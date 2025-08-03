package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.item.domain.SaleStatus;
import com.promesa.promesa.domain.item.dto.request.AddItemRequest;
import com.promesa.promesa.domain.item.dto.request.ItemImageRequest;
import com.promesa.promesa.domain.item.dto.request.UpdateItemRequest;
import com.promesa.promesa.domain.item.exception.DuplicateProductCodeException;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.exception.CategoryNotFoundException;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.item.validator.ItemValidator;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.itemCategory.service.ItemCategoryService;
import com.promesa.promesa.domain.member.domain.Member;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemQueryRepository itemQueryRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;
    private final ArtistRepository artistRepository;
    private final ItemRepository itemRepository;
    private final ImageService imageService;
    private final ItemValidator itemValidator;
    private final ItemImageService itemImageService;
    private final ItemCategoryService itemCategoryService;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;


    /**
     * 카테고리별 작품 조회
     * @param member
     * @param categoryId
     * @param pageable
     * @return
     */
    public Page<ItemPreviewResponse> findCategoryItem(Member member, Long categoryId, Pageable pageable) {
        if (categoryId != 0 && !categoryRepository.existsById(categoryId)) {  // categoryId == 0은 ALL 조회로 간주
            throw CategoryNotFoundException.EXCEPTION;
        }
        Page<ItemPreviewResponse> responses = itemQueryRepository.findCategoryItem(member, categoryId, pageable);

        return responses.map(r -> {
            String imageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
            return ItemPreviewResponse.of(r, imageUrl);
        });
    }

    /**
     * 작가의 카테고리별 작품 조회
     * @param member
     * @param artistId
     * @param categoryId
     * @return
     */
    public List<ItemPreviewResponse> findItemsByArtistAndCategory(Member member, Long artistId, Long categoryId, Pageable pageable) {
        if (!artistRepository.existsById(artistId)) {
            throw ArtistNotFoundException.EXCEPTION;
        }

        if (categoryId != 0) {  // categoryId == 0은 ALL 조회로 간주
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> CategoryNotFoundException.EXCEPTION);
        }

        List<ItemPreviewResponse> responses = itemQueryRepository.findItemsByArtistAndCategory(member, artistId, categoryId, pageable);
        List<ItemPreviewResponse> updated = responses.stream()
                .map(r -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
                    return ItemPreviewResponse.of(r, imageUrl);
                })
                .toList();

        return updated;
    }

    /**
     * 작품 등록
     * @param request
     * @return
     */
    @Transactional
    public String createItem(AddItemRequest request) {
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> CategoryNotFoundException.EXCEPTION);
        itemValidator.ensureProductCodeUnique(request.getProductCode(), null);

        Item newItem = Item.builder()  // 작품 객체 생성
                .name(request.getItemName())
                .price(request.getPrice())
                .stock(request.getStock())
                .saleStatus(SaleStatus.ON_SALE)
                .wishCount(0)
                .averageRating(null)
                .reviewCount(0)
                .totalRating(0)
                .productCode(request.getProductCode())
                .width(request.getWidth())
                .height(request.getHeight())
                .depth(request.getDepth())
                .artist(artist)
                .build();
        itemRepository.save(newItem);
        Long itemId = newItem.getId();

        // 아이템 이미지 생성
        itemImageService.uploadAndLinkImages(newItem, request.getImageKeys(), request.getThumbnailKey());

        // 아이템-카테고리 생성
        itemCategoryService.changeCategory(newItem, category);

        String message = "성공적으로 등록되었습니다.";
        return message;
    }

    /**
     * 작품 수정
     * @param request
     * @return
     */
    @Transactional
    public String updateItem(Long itemId, @Valid UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        itemValidator.validateSaleStatusChange(request.getSaleStatus(), item);

        // productCode 중복 검증(자기 자신 제외)
        itemValidator.ensureProductCodeUnique(request.getProductCode(), itemId);

        // 기본 속성 업데이트
        item.setName(request.getItemName());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        item.setSaleStatus(request.getSaleStatus());
        item.setProductCode(request.getProductCode());
        item.setWidth(request.getWidth());
        item.setHeight(request.getHeight());
        item.setDepth(request.getDepth());

        // 작가 변경
        if (!item.getArtist().getId().equals(request.getArtistId())) {    // 기존 작가랑 다르면
            Artist artist = artistRepository.findById(request.getArtistId())
                    .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);
            item.setArtist(artist);
        }


        Long currentCategoryId = item.getItemCategories().get(0).getCategory().getId(); // 현재 카테고리
        if (!request.getCategoryId().equals(currentCategoryId)) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> CategoryNotFoundException.EXCEPTION);
            itemCategoryService.changeCategory(item, newCategory);
        }

        // 이미지 전체 변경
        item.getItemImages().clear();
        itemImageService.uploadAndLinkImages(item, request.getImageKeys(), request.getThumbnailKey());

        return "성공적으로 수정되었습니다.";
    }
}

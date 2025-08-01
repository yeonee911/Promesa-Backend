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
import com.promesa.promesa.domain.item.exception.DuplicateProductCodeException;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.exception.CategoryNotFoundException;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.member.domain.Member;
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

        // productCode 중복인지
        if (itemRepository.existsByProductCode(request.getProductCode())) {
            throw DuplicateProductCodeException.EXCEPTION;
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> CategoryNotFoundException.EXCEPTION);

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
        for (ItemImageRequest itemImageRequest : request.getImageKeys()) {
            int sortOrder = itemImageRequest.sortOrder();
            boolean isThumbnail = itemImageRequest.key().equals(request.getThumbnailKey());

            String targetKey = imageService.transferImage(itemImageRequest.key(), itemId);

            ItemImage newItemImage = ItemImage.builder()
                    .imageKey(targetKey)
                    .isThumbnail(isThumbnail)
                    .sortOrder(sortOrder)
                    .item(newItem)
                    .build();
            newItem.addItemImage(newItemImage);
        }

        // 아이템-카테고리 생성
        ItemCategory newItemCategory = ItemCategory.builder()
                .category(category)
                .item(newItem)
                .build();
        newItem.addCategory(newItemCategory);

        String message = "성공적으로 등록되었습니다.";
        return message;
    }
}

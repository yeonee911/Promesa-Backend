package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.exception.CategoryNotFoundException;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemQueryRepository itemQueryRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;
    private final ArtistRepository artistRepository;

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
        return responses;
    }
}

package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.category.dao.CategoryRepository;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.exception.CategoryNotFoundException;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemQueryRepository itemQueryRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;


    public Page<ItemPreviewResponse> findCategoryItem(Member member, Long categoryId, Pageable pageable) {
        // 카테고리 존재 여부 검증
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> CategoryNotFoundException.EXCEPTION);

        Page<ItemPreviewResponse> responses = itemQueryRepository.findCategoryItem(member, categoryId, pageable);

        return responses.map(r -> {
            String imageUrl = s3Service.createPresignedGetUrl(bucketName, r.getImageUrl());
            return ItemPreviewResponse.of(r, imageUrl);
        });
    }
}

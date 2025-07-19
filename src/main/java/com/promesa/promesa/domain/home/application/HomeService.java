package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.domain.home.dto.response.BrandInfoResponse;
import com.promesa.promesa.domain.home.dto.response.ItemPreviewResponse;
import com.promesa.promesa.domain.home.dto.response.SearchResponse;
import com.promesa.promesa.domain.item.query.ItemQueryRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final S3Service s3Service;
    private final ArtistRepository artistRepository;
    private final WishRepository wishRepository;
    private final ItemQueryRepository itemQueryRepository;

    @Value("${aws.s3.bucket}")  // application.yml 에 정의 필요
    private String bucketName;

    private static final String MAIN_IMAGE = "brand-info/mainImage.png";
    private static final String LEFT_IMAGE = "brand-info/leftImage.png";
    private static final String RIGHT_IMAGE = "brand-info/rightImage.png";

    /**
     * 메인 화면에서 브랜드 정보 반환
     * @return
     */
    public BrandInfoResponse getBrandInfo() {
        String mainUrl = s3Service.createPresignedGetUrl(bucketName, MAIN_IMAGE);
        String leftUrl = s3Service.createPresignedGetUrl(bucketName, LEFT_IMAGE);
        String rightUrl = s3Service.createPresignedGetUrl(bucketName, RIGHT_IMAGE);
        return BrandInfoResponse.of(mainUrl, leftUrl, rightUrl);
    }

    /**
     * 작가/작품 검색
     * @param keyword (검색어)
     * @param member 로그인 사용자(위시리스트 조회용)
     * @return
     */
    public SearchResponse search(String keyword, Member member) {
        List<Artist> matchedArtists = artistRepository.findByNameContainingIgnoreCase(keyword);

        // 작가 검색
        List<ArtistResponse> artistResponses = matchedArtists.stream()
                .map(artist -> {
                    String presignedUrl = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());
                    boolean isWishlisted = (member != null) && wishRepository.existsByMemberAndTargetTypeAndTargetId(
                            member, TargetType.ARTIST, artist.getId()
                    );
                    return ArtistResponse.of(artist, presignedUrl, isWishlisted);
                })
                .toList();

        // 작품 검색
        String sanitizedKeyword = keyword.replaceAll("\\s+", "");
        List<ItemPreviewResponse> itemResponses = itemQueryRepository.searchByItemName(sanitizedKeyword, member).stream()
                .map(item -> {
                    String imageUrl = s3Service.createPresignedGetUrl(bucketName, item.getImageUrl());
                    return ItemPreviewResponse.of(item, imageUrl);
                })
                .toList();

        return SearchResponse.builder()
                .artists(artistResponses)
                .itemPreviews(itemResponses)
                .build();
    }
}

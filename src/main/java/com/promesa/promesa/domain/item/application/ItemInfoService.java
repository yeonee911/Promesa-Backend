package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.ArtistProfile;
import com.promesa.promesa.domain.artist.dto.ArtistWish;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.dto.ItemDetail;
import com.promesa.promesa.domain.item.dto.ItemResponse;
import com.promesa.promesa.domain.item.dto.ItemSale;
import com.promesa.promesa.domain.item.dto.ItemSummary;
import com.promesa.promesa.domain.item.dto.ItemWish;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemInfoService {

    private final ItemRepository itemRepository;
    private final ArtistRepository artistRepository;
    private final WishRepository wishRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public ItemResponse getItemResponse(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        Artist artist = artistRepository.findById(item.getArtist().getId())
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        // 카테고리
        ItemCategory itemCategory = item.getItemCategories().get(0);
        Long categoryId = itemCategory.getCategory().getId();
        String categoryName = itemCategory.getCategory().getName();

        // 이미지 Presigned URL
        List<String> imageUrls = item.getItemImages().stream()
                .map(image -> s3Service.createPresignedGetUrl(bucketName, image.getImageKey()))
                .toList();

        String artistImageUrl = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());

        // DTO 조립
        ItemSummary itemSummary = new ItemSummary(
                item.getId(),
                categoryId,
                categoryName,
                item.getName(),
                imageUrls,
                item.getAverageRating(),
                item.getReviewCount(),
                artist.getId()
        );

        ItemDetail itemDetail = new ItemDetail(
                item.getProductCode(),
                categoryName,
                item.getWidth(),
                item.getHeight(),
                item.getDepth()
        );

        // 위시 처리 - member가 null인지 먼저 체크
        boolean itemWished = (member != null) &&
                wishRepository.existsByMemberAndTargetTypeAndTargetId(member, TargetType.ITEM, itemId);
        int itemWishCount = wishRepository.countByTargetTypeAndTargetId(TargetType.ITEM, itemId);
        ItemWish itemWish = new ItemWish(itemWished, itemWishCount);

        ItemSale itemSale = new ItemSale(
                item.getStock(),
                item.getStock() == 0,
                item.getPrice(),
                item.getPrice() >= 70000,
                "제주/도서산간 3,000원 추가"
        );

        boolean artistWished = (member != null) &&
                wishRepository.existsByMemberAndTargetTypeAndTargetId(member, TargetType.ARTIST, artist.getId());
        int artistWishCount = wishRepository.countByTargetTypeAndTargetId(TargetType.ARTIST, artist.getId());

        ArtistProfile artistProfile = new ArtistProfile(
                artist.getId(),
                artist.getName(),
                artistImageUrl,
                artist.getDescription(),
                "https://instagram.com/" + artist.getInsta()
        );
        ArtistWish artistWish = new ArtistWish(artistWished, artistWishCount);

        return new ItemResponse(
                itemSummary,
                itemDetail,
                itemWish,
                artistProfile,
                artistWish,
                itemSale
        );
    }

}

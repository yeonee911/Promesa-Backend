package com.promesa.promesa.domain.item.application;

import com.promesa.promesa.common.advice.ItemLog;
import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.item.dto.response.ItemImageResponse;
import com.promesa.promesa.domain.item.dto.response.ItemResponse;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.itemCategory.domain.ItemCategory;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemInfoService {

    private final ItemRepository itemRepository;
    private final ArtistRepository artistRepository;
    private final WishRepository wishRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @ItemLog
    public ItemResponse getItemResponse(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        Artist artist = artistRepository.findById(item.getArtist().getId())
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        ItemCategory itemCategory = item.getItemCategories().get(0);

        // 이미지 Presigned URL
        List<ItemImageResponse> mainImageUrls = getPresignedImageUrlsByType(item, "main");
        List<ItemImageResponse> detailImageUrls = getPresignedImageUrlsByType(item, "detail");

        // 작가 이미지 Presigned URL
        String artistImageUrl = getPresignedArtistImageUrl(artist);

        // 위시 상태 & 개수
        boolean itemWished = isWished(member, TargetType.ITEM, item.getId());

        boolean artistWished = isWished(member, TargetType.ARTIST, artist.getId());

        return ItemResponse.of(
                item,
                itemCategory.getCategory(),
                mainImageUrls,
                detailImageUrls,
                artist,
                artistImageUrl,
                itemWished,
                artistWished
        );

    }

    private List<ItemImageResponse> getPresignedImageUrlsByType(Item item, String type) {
        return Optional.ofNullable(item.getItemImages())
                .orElse(List.of())
                .stream()
                .filter(img -> {
                    String key = img.getImageKey();
                    return key != null && key.contains("/" + type + "/");
                })
                .sorted(Comparator.comparing(ItemImage::getSortOrder))
                .map(img -> new ItemImageResponse(
                        s3Service.createPresignedGetUrl(bucketName, img.getImageKey()),
                        img.getSortOrder()
                ))
                .toList();
    }


    private String getPresignedArtistImageUrl(Artist artist) {
        String key = artist.getProfileImageKey();
        return (key != null && !key.isBlank()) ? s3Service.createPresignedGetUrl(bucketName, key) : null;
    }

    private boolean isWished(Member member, TargetType type, Long targetId) {
        return member != null &&
                wishRepository.existsByMemberAndTargetTypeAndTargetId(member, type, targetId);
    }
}

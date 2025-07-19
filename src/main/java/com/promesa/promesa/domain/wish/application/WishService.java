package com.promesa.promesa.domain.wish.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.ArtistWish;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.domain.ItemImage;
import com.promesa.promesa.domain.item.dto.ItemWish;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;
import com.promesa.promesa.domain.wish.dto.WishResponse;
import com.promesa.promesa.domain.wish.dto.WishToggleResponse;
import com.promesa.promesa.domain.wish.exception.DuplicatedWishException;
import com.promesa.promesa.domain.wish.exception.UnsupportedTargetTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private final ArtistRepository artistRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 접근 허용
    @Transactional
    public WishToggleResponse addWish(Member member, TargetType targetType, Long targetId) {
        boolean exists = wishRepository.existsByMemberAndTargetTypeAndTargetId(member, targetType, targetId);
        if (exists) {
            throw DuplicatedWishException.EXCEPTION;
        }

        validateTargetExistence(targetType, targetId);

        Wish wish = Wish.builder()
                .member(member)
                .targetType(targetType)
                .targetId(targetId)
                .build();
        wishRepository.save(wish);

        // wishCount 증가
        int wishCount;
        if (targetType == TargetType.ITEM) {
            Item item = itemRepository.findById(targetId)
                    .orElseThrow(() -> ItemNotFoundException.EXCEPTION);
            item.increaseWishCount();
            wishCount = item.getWishCount();
        } else if (targetType == TargetType.ARTIST) {
            Artist artist = artistRepository.findById(targetId)
                    .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);
            artist.increaseWishCount();
            wishCount = artist.getWishCount();
        } else {
            throw UnsupportedTargetTypeException.EXCEPTION;
        }

        return WishToggleResponse.builder()
                .message("위시리스트에 추가되었습니다.")
                .wished(true)
                .target(
                        WishToggleResponse.Target.builder()
                                .targetType(targetType)
                                .targetId(targetId)
                                .wishCount(wishCount)
                                .build()
                )
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public WishToggleResponse deleteWish(Member member, TargetType targetType, Long targetId) {
        validateTargetExistence(targetType, targetId);

        wishRepository.deleteByMemberAndTargetTypeAndTargetId(member, targetType, targetId);

        // wishCount 감소
        int wishCount;
        if (targetType == TargetType.ITEM) {
            Item item = itemRepository.findById(targetId)
                    .orElseThrow(() -> ItemNotFoundException.EXCEPTION);
            item.decreaseWishCount();
            wishCount = item.getWishCount();
        } else if (targetType == TargetType.ARTIST) {
            Artist artist = artistRepository.findById(targetId)
                    .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);
            artist.decreaseWishCount();
            wishCount = artist.getWishCount();
        } else {
            throw UnsupportedTargetTypeException.EXCEPTION;
        }

        return WishToggleResponse.builder()
                .message("위시리스트에서 삭제되었습니다.")
                .wished(false)
                .target(
                        WishToggleResponse.Target.builder()
                                .targetType(targetType)
                                .targetId(targetId)
                                .wishCount(wishCount)
                                .build()
                )
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public List<WishResponse> getWishList(Member member, TargetType targetType) {
        List<Wish> wishes = wishRepository.findByMemberAndTargetType(member, targetType);

        return wishes.stream()
                .map(wish -> {
                    // Item 위시리스트 목록
                    if (targetType == TargetType.ITEM) {
                        Item item = itemRepository.findById(wish.getTargetId())
                                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

                        String title = item.getName();

                        // 썸네일
                        String thumbnailUrl = item.getItemImages().stream()
                                .filter(ItemImage::isThumbnail)
                                .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                                .findFirst()
                                .orElse(null);  // 없으면 null 반환

                        String artistName = item.getArtist().getName();
                        int price = item.getPrice();

                        return WishResponse.fromItem(wish, title, thumbnailUrl, artistName, price);
                    }

                    // Artist 위시리스트 목록
                    else if (targetType == TargetType.ARTIST) {
                        Artist artist = artistRepository.findById(wish.getTargetId())
                                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

                        String thumbnailUrl = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());

                        return WishResponse.fromArtist(wish, artist.getName(), thumbnailUrl);
                    }

                    else {
                        throw UnsupportedTargetTypeException.EXCEPTION;
                    }
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemWish getItemWish(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

        boolean isWishlisted = false;
        if (member != null) {
            isWishlisted = wishRepository.existsByMemberAndTargetTypeAndTargetId(member, TargetType.ITEM, itemId);
        }

        return ItemWish.from(item, isWishlisted);
    }

    @Transactional(readOnly = true)
    public ArtistWish getArtistWish(Long artistId, Member member) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        boolean isWishlisted = false;
        if (member != null) {
            isWishlisted = wishRepository.existsByMemberAndTargetTypeAndTargetId(member, TargetType.ARTIST, artistId);
        }

        return ArtistWish.from(artist, isWishlisted);
    }

    // 실제 존재하는 target인지 검증
    private void validateTargetExistence(TargetType targetType, Long targetId) {
        switch (targetType) {
            case ITEM -> {
                if (!itemRepository.existsById(targetId)) {
                    throw ItemNotFoundException.EXCEPTION;
                }
            }
            case ARTIST -> {
                if (!artistRepository.existsById(targetId)) {
                    throw ArtistNotFoundException.EXCEPTION;
                }
            }
            default -> throw UnsupportedTargetTypeException.EXCEPTION;
        }
    }
}



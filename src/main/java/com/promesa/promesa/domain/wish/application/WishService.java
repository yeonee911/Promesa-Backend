package com.promesa.promesa.domain.wish.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.item.exception.ItemNotFoundException;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import com.promesa.promesa.domain.wish.domain.Wish;
import com.promesa.promesa.domain.wish.dto.WishResponse;
import com.promesa.promesa.domain.wish.exception.UnsupportedTargetTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
    public void addWish(Member member, TargetType targetType, Long targetId) {
        log.info("🧾 addWish 호출 - member_id: {}, target_id: {}, target_type: {}",
                member.getId(), targetId, targetType);
        boolean exists = wishRepository.existsByMemberAndTargetTypeAndTargetId(member, targetType, targetId);
        if (exists) return; // 이미 위시에 존재하는 경우 return

        validateTargetExistence(targetType, targetId);

        Wish wish = Wish.builder()
                .member(member)
                .targetType(targetType)
                .targetId(targetId)
                .build();
        wishRepository.save(wish);
    }

    @PreAuthorize("isAuthenticated()")
    public void deleteWish(Member member, TargetType targetType, Long targetId) {

        validateTargetExistence(targetType, targetId);

        wishRepository.deleteByMemberAndTargetTypeAndTargetId(member, targetType, targetId);
    }

    @PreAuthorize("isAuthenticated()")
    public List<WishResponse> getWishList(Member member, TargetType targetType) {
        List<Wish> wishes = wishRepository.findByMemberAndTargetType(member, targetType);

        return wishes.stream()
                .map(wish -> {
                    // Item 위시리스트 목록
                    if (targetType == TargetType.ITEM) {
                        Item item = itemRepository.findById(wish.getTargetId())
                                .orElseThrow(() -> ItemNotFoundException.EXCEPTION);

                        String title = item.getName();

                        // 썸네일 = main 이미지 중 첫 번째 이미지
                        String thumbnailUrl = item.getItemImages().stream()
                                .filter(img -> img.getImageKey().contains("/main/"))
                                .map(img -> s3Service.createPresignedGetUrl(bucketName, img.getImageKey()))
                                .findFirst()
                                .orElse(null);  // 없으면 null 반환

                        return WishResponse.from(wish, title, thumbnailUrl);
                    }

                    // Artist 위시리스트 목록
                    else if (targetType == TargetType.ARTIST) {
                        Artist artist = artistRepository.findById(wish.getTargetId())
                                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

                        String thumbnailUrl = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());

                        return WishResponse.from(wish, artist.getName(), thumbnailUrl);
                    }

                    else {
                        throw UnsupportedTargetTypeException.EXCEPTION;
                    }
                })
                .toList();
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



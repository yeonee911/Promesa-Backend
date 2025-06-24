package com.promesa.promesa.domain.artist.application;

import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    /*
    public ArtistResponse getArtistProfile(Long artistId){
        // 작가 존재 여부 검증
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(()->ArtistNotFoundException.EXCEPTION);

        return ArtistResponse.from(artist);
    }
     */

    public ArtistResponse getArtistProfile(Long artistId, OAuth2User user){
        log.info("OAuth2User: {}", user);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        boolean isWishlisted = false;

        if (user != null) {
            String provider = (String) user.getAttribute("provider");
            String providerId = (String) user.getAttribute("providerId");

            Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

            isWishlisted = wishRepository.existsByMemberAndTargetTypeAndTargetId(
                    member, TargetType.ARTIST, artist.getId()
            );
        }

        return ArtistResponse.from(artist,isWishlisted);
    }
}

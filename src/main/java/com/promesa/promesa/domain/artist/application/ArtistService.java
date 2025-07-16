package com.promesa.promesa.domain.artist.application;

import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.ArtistNameResponse;
import com.promesa.promesa.domain.artist.dto.ArtistResponse;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public ArtistResponse getArtistProfile(Long artistId, Member member) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        String presignedUrl = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());

        boolean isWishlisted = false;
        if (member != null) {
            isWishlisted = wishRepository.existsByMemberAndTargetTypeAndTargetId(
                    member, TargetType.ARTIST, artist.getId()
            );
        }

        return ArtistResponse.of(artist, presignedUrl, isWishlisted);
    }

    public List<ArtistResponse> getAllArtists(Member member) {
        List<Artist> artists = artistRepository.findAll();

        return artists.stream()
                .map(artist -> {
                    String url = s3Service.createPresignedGetUrl(bucketName, artist.getProfileImageKey());
                    boolean isWishlisted = (member != null) && wishRepository.existsByMemberAndTargetTypeAndTargetId(
                            member, TargetType.ARTIST, artist.getId()
                    );
                    return ArtistResponse.of(artist, url, isWishlisted);
                })
                .toList();
    }

    public List<ArtistNameResponse> getArtistNames() {
        return artistRepository.findAll().stream()
                .map(ArtistNameResponse::from)
                .toList();
    }

}

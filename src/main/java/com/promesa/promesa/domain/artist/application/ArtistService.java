package com.promesa.promesa.domain.artist.application;

import com.promesa.promesa.common.application.ImageService;
import com.promesa.promesa.common.application.S3Service;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.dto.request.AddArtistRequest;
import com.promesa.promesa.domain.artist.dto.request.UpdateArtistInfoRequest;
import com.promesa.promesa.domain.artist.dto.response.ArtistNameResponse;
import com.promesa.promesa.domain.artist.dto.response.ArtistResponse;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.artist.exception.DuplicateArtistForMemberException;
import com.promesa.promesa.domain.artist.exception.DuplicateArtistNameException;
import com.promesa.promesa.domain.artist.validator.ArtistValidator;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.domain.Role;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import com.promesa.promesa.domain.wish.dao.WishRepository;
import com.promesa.promesa.domain.wish.domain.TargetType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final S3Service s3Service;
    private final ImageService imageService;
    private final ArtistValidator artistValidator;

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

    /**
     * 작가 등록
     * @param request
     * @return
     */
    @Transactional
    public String createArtist(@Valid AddArtistRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        if (artistRepository.existsByMember(member)) {
            throw DuplicateArtistForMemberException.EXCEPTION;
        }
        artistValidator.ensureArtistNameUnique(request.getArtistName(), null);

        Artist newArtist = Artist.builder()
                .name(request.getArtistName())
                .subname(request.getSubName())
                .profileImageKey(request.getProfileKey())
                .description(request.getDescription())
                .insta(request.getInsta())
                .wishCount(0)
                .member(member)
                .build();

        newArtist.setMember(member);
        artistRepository.save(newArtist);

        member.addRole(Role.ROLE_ARTIST); // 작가 권한 등록
        memberRepository.save(member);

        Long artistId = newArtist.getId();
        String targetKey = imageService.transferImage(newArtist.getProfileImageKey(), artistId);
        newArtist.setProfileImageKey(targetKey);    // db에 키 없데이트
        artistRepository.save(newArtist);

        String message = "성공적으로 등록되었습니다.";
        return message;
    }

    /**
     * 작가 정보 수정
     * @param artistId
     * @param request
     * @return
     */
    @Transactional
    public String updateArtistInfo(Long artistId, UpdateArtistInfoRequest request) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        if (request.getArtistName() != null) {
            // 자기 자신을 제외한 중복 체크
            artistValidator.ensureArtistNameUnique(request.getArtistName(), artist.getId());
            artist.setName(request.getArtistName());
        }

        if (request.getSubName() != null) {
            artist.setSubname(request.getSubName());
        }

        if (request.getDescription() != null) {
            artist.setDescription(request.getDescription());
        }

        if (request.getInsta() != null) {
            artist.setInsta(request.getInsta());
        }

        artistRepository.save(artist);
        return "성공적으로 수정되었습니다.";
    }
}

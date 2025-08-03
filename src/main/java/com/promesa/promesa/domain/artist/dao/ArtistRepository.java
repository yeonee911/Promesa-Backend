package com.promesa.promesa.domain.artist.dao;

import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String keyword);

    boolean existsByMember(Member member);

    boolean existsByName(@NotBlank(message = "작가명은 필수입니다") String artistName);

    boolean existsByNameAndIdNot(String artistName, Long artistId);
}

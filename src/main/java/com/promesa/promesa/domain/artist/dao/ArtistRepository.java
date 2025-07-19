package com.promesa.promesa.domain.artist.dao;

import com.promesa.promesa.domain.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String keyword);
}

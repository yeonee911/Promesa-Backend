package com.promesa.promesa.domain.artist.dao;

import com.promesa.promesa.domain.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}

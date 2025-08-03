package com.promesa.promesa.domain.artist.validator;

import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.exception.DuplicateArtistNameException;
import com.promesa.promesa.domain.item.exception.DuplicateProductCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistValidator {
    private final ArtistRepository artistRepository;

    /**
     * 작가명 중복 방지
     * @param name
     * @param existingArtistId
     */
    public void ensureArtistNameUnique(String name, Long existingArtistId) {
        boolean exists = existingArtistId == null
                ? artistRepository.existsByName(name)
                : artistRepository.existsByNameAndIdNot(name, existingArtistId);
        if (exists) {
            throw DuplicateArtistNameException.EXCEPTION;
        }
    }
}

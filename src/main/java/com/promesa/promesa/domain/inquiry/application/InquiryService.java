package com.promesa.promesa.domain.inquiry.application;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.category.domain.Category;
import com.promesa.promesa.domain.category.exception.CategoryNotFoundException;
import com.promesa.promesa.domain.inquiry.dao.InquiryRepository;
import com.promesa.promesa.domain.inquiry.dto.InquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public List<InquiryResponse> getInquiriesByArtist(Long artistId) {
        // 작가 존재 여부 검증
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        return inquiryRepository.findAllByArtistId(artistId).stream()
                .map(InquiryResponse::of)
                .toList();
    }
}


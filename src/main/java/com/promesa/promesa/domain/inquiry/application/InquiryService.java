package com.promesa.promesa.domain.inquiry.application;
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

    @Transactional(readOnly = true)
    public List<InquiryResponse> getInquiriesByArtist(Long artistId) {
        return inquiryRepository.findAllByArtistId(artistId).stream()
                .map(inquiry -> new InquiryResponse(
                        inquiry.getInquiryId(),
                        inquiry.getQuestion(),
                        inquiry.getAnswer()))
                .toList();
    }
}


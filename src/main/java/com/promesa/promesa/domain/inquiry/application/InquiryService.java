package com.promesa.promesa.domain.inquiry.application;
import com.promesa.promesa.domain.artist.dao.ArtistRepository;
import com.promesa.promesa.domain.artist.domain.Artist;
import com.promesa.promesa.domain.artist.exception.ArtistNotFoundException;
import com.promesa.promesa.domain.inquiry.dao.InquiryRepository;
import com.promesa.promesa.domain.inquiry.domain.Inquiry;
import com.promesa.promesa.domain.inquiry.dto.request.AddInquiryRequest;
import com.promesa.promesa.domain.inquiry.dto.request.UpdateInquiryRequest;
import com.promesa.promesa.domain.inquiry.dto.response.InquiryResponse;
import com.promesa.promesa.domain.inquiry.exception.InquiryNotFoundException;
import jakarta.validation.Valid;
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

    /**
     * 관리자가 문의 등록
     * @param request
     * @return
     */
    @Transactional
    public String createInquiry(@Valid AddInquiryRequest request) {
        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> ArtistNotFoundException.EXCEPTION);

        Inquiry newInquiry = Inquiry.builder()
                .artist(artist)
                .question(request.question())
                .answer(request.answer())
                .build();
        inquiryRepository.save(newInquiry);

        String message = "성공적으로 등록되었습니다.";
        return message;
    }

    /**
     * 관리자가 문의 삭제
     * @param inquiryId
     * @return
     */
    @Transactional
    public String deleteInquiry(Long inquiryId) {
        inquiryRepository.deleteById(inquiryId);

        String message = "성공적으로 삭제되었습니다.";
        return message;
    }

    /**
     * 관리자가 문의 등록
     * @param inquiryId
     * @param request
     * @return
     */
    @Transactional
    public String updateInquiry(Long inquiryId, @Valid UpdateInquiryRequest request) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(()-> InquiryNotFoundException.EXCEPTION);
        inquiry.updateQuestion(request.question());
        inquiry.updateAnswer(request.answer());
        inquiryRepository.save(inquiry);

        String message = "성공적으로 수정되었습니다.";
        return message;
    }
}


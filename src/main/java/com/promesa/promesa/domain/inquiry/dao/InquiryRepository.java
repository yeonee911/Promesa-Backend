package com.promesa.promesa.domain.inquiry.dao;

import com.promesa.promesa.domain.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByArtistId(Long artistId);
}

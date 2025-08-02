package com.promesa.promesa.domain.exhibition.dao;

import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    List<Exhibition> findAllByStatus(ExhibitionStatus status);

    List<Exhibition> findAllByStatusIn(List<ExhibitionStatus> statuses);

    boolean existsByTitle(@NotBlank(message = "제목은 필수입니다") String title);
}

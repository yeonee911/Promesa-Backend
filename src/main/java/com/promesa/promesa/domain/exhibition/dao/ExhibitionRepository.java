package com.promesa.promesa.domain.exhibition.dao;

import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

}

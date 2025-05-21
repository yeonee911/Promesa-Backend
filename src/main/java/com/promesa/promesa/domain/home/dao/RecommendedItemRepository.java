package com.promesa.promesa.domain.home.dao;

import com.promesa.promesa.domain.home.domain.RecommendedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedItemRepository extends JpaRepository<RecommendedItem, Long> {
    List<RecommendedItem> findTop15ByOrderByPriorityAsc();
}
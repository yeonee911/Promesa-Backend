package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.query.ItemQueryRepository;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ItemQueryRepository itemQueryRepository;

    public List<ItemPreviewResponse> getExhibitionItems(Long memberId, Long exhibitionId) {
        return itemQueryRepository.findExhibitionItem(memberId, exhibitionId);
    }
}

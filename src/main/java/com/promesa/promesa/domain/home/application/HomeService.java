package com.promesa.promesa.domain.home.application;

import com.promesa.promesa.common.query.ItemQueryRepository;
import com.promesa.promesa.domain.home.dao.ExhibitionRepository;
import com.promesa.promesa.domain.home.dto.ItemPreviewResponse;
import com.promesa.promesa.domain.home.exception.ExhibitionNotFoundException;
import com.promesa.promesa.domain.item.domain.Exhibition;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ItemQueryRepository itemQueryRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final MemberRepository memberRepository;

    public List<ItemPreviewResponse> getExhibitionItems(Long memberId, Long exhibitionId) {
        // 기획전 존재 여부 검증
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> ExhibitionNotFoundException.EXCEPTION);
        // member 존재 여부 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        return itemQueryRepository.findExhibitionItem(memberId, exhibitionId);
    }
}

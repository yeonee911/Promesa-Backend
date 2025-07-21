package com.promesa.promesa.domain.exhibition.application;

import com.promesa.promesa.domain.exhibition.dao.ExhibitionRepository;
import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExhibitionStatusScheduler {

    private final ExhibitionRepository exhibitionRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")   // 매일 오전 12시
    public void updateExhibitionStatus() {
        List<Exhibition> exhibitions = exhibitionRepository.findAllByStatusIn(List.of(UPCOMING, ONGOING));
        LocalDate today = LocalDate.now();

        for (Exhibition exhibition : exhibitions) {
            if (exhibition.getStatus() == UPCOMING && today.isAfter(exhibition.getStartDate())) {
                if (exhibition.getEndDate() == null) exhibition.setStatus(PERMANENT);
                else exhibition.setStatus(ONGOING);
            }
            else if (exhibition.getStatus() == ONGOING && today.isAfter(exhibition.getEndDate())) {
                exhibition.setStatus(PAST);
            }
        }

        log.info("Exhibition 상태 업데이트 완료 (총 {}건)", exhibitions.size());
    }
}

package com.promesa.promesa.domain.exhibition.query;

import com.promesa.promesa.domain.exhibition.domain.Exhibition;
import com.promesa.promesa.domain.exhibition.domain.ExhibitionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.promesa.promesa.domain.artist.domain.QArtist.artist;
import static com.promesa.promesa.domain.exhibition.domain.QExhibition.exhibition;
import static com.promesa.promesa.domain.exhibition.domain.QExhibitionArtist.exhibitionArtist;

@Repository
@RequiredArgsConstructor
public class ExhibitionQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Exhibition> findByArtistId(Long artistId) {
        return queryFactory
                .select(exhibition)
                .from(exhibitionArtist)
                .join(exhibitionArtist.exhibition, exhibition)
                .join(exhibitionArtist.artist, artist)
                .where(artist.id.eq(artistId))
                .fetch();
    }

    public List<Exhibition> findByStatusOrderByStartDateDesc(ExhibitionStatus status) {
        return queryFactory
                .selectFrom(exhibition)
                .where(status != null ? exhibition.status.eq(status) : null)
                .orderBy(exhibition.startDate.desc())
                .fetch();
    }

    public List<String> findArtistNames(Long exhibitionId) {
        List<String> artistNames = queryFactory
                .select(artist.name)
                .distinct()
                .from(exhibitionArtist)
                .join(exhibitionArtist.artist, artist)
                .where(exhibitionArtist.exhibition.id.eq(exhibitionId))
                .fetch();
        return artistNames;
    }
}

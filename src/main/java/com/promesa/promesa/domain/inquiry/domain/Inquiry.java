package com.promesa.promesa.domain.inquiry.domain;

import com.promesa.promesa.common.domain.BaseTimeEntity;
import com.promesa.promesa.domain.artist.domain.Artist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="inquiry")
public class Inquiry extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Builder
    public Inquiry(String question, String answer, Artist artist) {
        this.question = question;
        this.answer = answer;
        this.artist = artist;
    }

    public void updateQuestion(String question) {
        this.question = question;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

}


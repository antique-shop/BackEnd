package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reviewer_id", nullable = false)
    private User reviewer; // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reviewed_user_id", nullable = false)
    private User reviewedUser; // 리뷰 대상자

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @ToString.Exclude
    private Product product;

    @Column
    private int rating;

    @Column
    private String content;

    @Column
    private LocalDateTime reviewDate;
}


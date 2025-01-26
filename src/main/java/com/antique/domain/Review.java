package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE review SET is_deleted = true WHERE reviewId = ?")
@Where(clause = "is_deleted = false")
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

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // 빌더 클래스
    public static class Builder {
        private User reviewer;
        private User reviewedUser;
        private Product product;
        private int rating;
        private String content;
        private LocalDateTime reviewDate = LocalDateTime.now();

        public Builder reviewer(User reviewer) {
            this.reviewer = reviewer;
            return this;
        }

        public Builder reviewedUser(User reviewedUser) {
            this.reviewedUser = reviewedUser;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder reviewDate(LocalDateTime reviewDate) {
            this.reviewDate = reviewDate;
            return this;
        }

        public Review build() {
            Review review = new Review();
            review.reviewer = this.reviewer;
            review.reviewedUser = this.reviewedUser;
            review.product = this.product;
            review.rating = this.rating;
            review.content = this.content;
            review.reviewDate = this.reviewDate;
            return review;
        }
    }
}


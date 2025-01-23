package com.antique.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    @NotNull
    private Long reviewerId; // 리뷰 작성자 ID

    @NotNull
    private Long reviewedUserId; // 리뷰 대상자 ID

    @NotNull
    private Long productId; // 제품 ID

    @NotNull
    private int rating; // 평점

    private String content; // 리뷰 내용
}

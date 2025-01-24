package com.antique.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReviewDTO {
    @Schema(description = "상품명")
    private String productName;    // 상품명

    @Schema(description = "리뷰 작성자 닉네임")
    private String nickname;      // 리뷰 작성자 닉네임

    @Schema(description = "리뷰 내용")
    private String content;       // 리뷰 내용

    @Schema(description = "리뷰 작성 날짜")
    private LocalDateTime reviewDate; // 리뷰 작성 날짜

    @Schema(description = "리뷰 평점")
    private int rating;           // 리뷰 평점
}

package com.antique.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {

    @Schema(description = "리뷰 대상자 ID")
    @NotNull
    private Long reviewedUserId; // 리뷰 대상자 ID

    @Schema(description = "제품 ID")
    @NotNull
    private Long productId; // 제품 ID

    @Schema(description = "평점")
    @NotNull
    private int rating; // 평점

    @Schema(description = "리뷰 내용")
    private String content; // 리뷰 내용
}

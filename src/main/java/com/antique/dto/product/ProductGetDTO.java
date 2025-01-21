package com.antique.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetDTO {
    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "상품 상세 설명")
    private String description;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 상태", example = "AVAILABLE")
    private String status;

    @Schema(description = "상품 이미지")
    private List<String> productImages; // 이미지 URL 리스트
}

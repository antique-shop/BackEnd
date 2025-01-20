package com.antique.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductUpdateDTO {

    @Schema(description = "user id", example = "1", required = true)
    private Long userId;

    @Schema(description = "product id", example = "101", required = true)
    private Long productId;

    @Schema(description = "상품명", example = "Vintage Pants", required = true)
    private String name;

    @Schema(description = "상품 설명", example = "A beautiful vintage pants", required = true)
    private String description;

    @Schema(description = "상품 가격", example = "120,000", required = true)
    private int price;

    @Schema(description = "카테고리 ID", example = "1", required = true)
    private Long categoryId;

    @Schema(description = "상품 이미지 URL 리스트", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]", required = false)
    private List<String> images;
}

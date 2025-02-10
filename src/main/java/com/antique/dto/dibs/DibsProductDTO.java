package com.antique.dto.dibs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DibsProductDTO {

    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품 이미지")
    private List<String> productImages;
}

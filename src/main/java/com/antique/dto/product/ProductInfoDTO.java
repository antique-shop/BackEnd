package com.antique.dto.product;

import com.antique.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDTO {
    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 상세 설명")
    private String description;

    @Schema(description = "상품 가격")
    private String category;

    @Schema(description = "상품 이미지 리스트")
    private List<String> productImages;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 판매자 닉네임")
    private String sellerNickname;

    @Schema(description = "상품 판매자 평점")
    private double sellerRating;

    public ProductInfoDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory().getCategoryName().toString();
        this.productImages = product.getProductImages().stream()
                .map(productImage -> productImage.getProductImageUrl())
                .collect(Collectors.toList());
        this.price = product.getPrice();
        this.sellerNickname = product.getSeller().getNickname();
        this.sellerRating = product.getSeller().getRating();
    }
}

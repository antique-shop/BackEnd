package com.antique.dto;

import com.antique.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDTO {
    private Long productId;

    private String productName;

    private String description;

    private String category;

    private List<String> productImages;

    private int price;

    private String sellerNickname;

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

package com.antique;

import com.antique.domain.*;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.dto.user.UserRequestDTO;

import java.time.LocalDateTime;
import java.util.List;


public class TestDataFactory {
    public static User createUser(Long userId, String email, String nickname) {
        return User.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public static User createUserWithDefault(Long userId) {
        return createUser(userId, "default@example.com", "defaultNickname");
    }

    public static UserRequestDTO createUserRequestDTO(String nickname, String address) {
        return UserRequestDTO.builder()
                .nickname(nickname)
                .address(address)
                .build();
    }

    public static UserRequestDTO createUserRequestDTOWithDefaults() {
        return createUserRequestDTO("defaultNickname", "defaultAddress");
    }

    // ProductRequestDTO 생성
    public static ProductRequestDTO createProductRequestDTO(String name, String description, int price, Long categoryId, List<String> images) {
        return ProductRequestDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .images(images)
                .build();
    }



    public static ProductRequestDTO createProductRequestDTOWithDefaults(Long categoryId) {
        return createProductRequestDTO(
                "Default Product Name",
                "Default Description",
                100000,
                categoryId,
                List.of("https://example.com/default-image1.jpg", "https://example.com/default-image2.jpg")
        );
    }

    // ProductUpdateDTO 생성
    public static ProductUpdateDTO createProductUpdateDTO(Long productId, Long categoryId, String name, String description, int price, List<String> images) {
        return ProductUpdateDTO.builder()
                .productId(productId)
                .categoryId(categoryId)
                .name(name)
                .description(description)
                .price(price)
                .images(images)
                .build();
    }

    public static ProductUpdateDTO createProductUpdateDTOWithDefaults(Long productId, Long categoryId) {
        return createProductUpdateDTO(
                productId,
                categoryId,
                "Default Product Name",
                "Default Description",
                100000,
                List.of("https://example.com/default-image1.jpg", "https://example.com/default-image2.jpg")
        );
    }

    // Product 생성
    public static Product createProduct(Long productId, String name, String description, int price) {
        return Product.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

    public static Product createProductWithDefaults(Long productId, User seller, Category category) {
        List<ProductImage> productImages = List.of(
                ProductImage.builder().productImageUrl("https://example.com/default-image1.jpg").build(),
                ProductImage.builder().productImageUrl("https://example.com/default-image2.jpg").build()
        );

        return Product.builder()
                .productId(productId)
                .name("Default Product Name")
                .description("Default Description")
                .price(100000)
                .status(Product.Status.AVAILABLE)
                .category(category)
                .seller(seller)
                .productImages(productImages)
                .status(Product.Status.AVAILABLE)
                .build();
    }

    // Category 생성
    public static Category createCategory(Long categoryId, Category.CategoryName categoryName) {
        return Category.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();
    }

    // Review 생성
    public static Review createReview(Product product, User reviewer,User reviewedUser, String content, LocalDateTime reviewDate, int rating) {
        return new Review.Builder()
                .product(product)
                .reviewer(reviewer)
                .reviewedUser(reviewedUser)
                .content(content)
                .reviewDate(reviewDate)
                .rating(rating)
                .build();
    }
}
package com.antique.domain;

import com.antique.dto.product.ProductUpdateDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE productId = ?")
@Where(clause = "is_deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "sellerUserId", nullable = false)
    @ToString.Exclude
    private User seller; // 판매자와 관계

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    @ToString.Exclude
    private Category category; // 카테고리와 관계

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Dibs> dibs;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SalesProduct> salesProducts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    public Product(Long productId, String name, String description, Category category, int price, List<String> images, String sellerNickname, float sellerRating, User seller) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productImages = images.stream()
                .map(imageUrl -> new ProductImage(null, this, imageUrl)) // ProductImage 생성자에 맞게 수정
                .collect(Collectors.toList());
        this.seller = seller;
        this.seller.setNickname(sellerNickname);
        this.seller.setRating(sellerRating);
    }

    // Enum 정의
    public enum Status {
        AVAILABLE, SOLD_OUT
    }

    public Product(Long productId, String name, String description, int price, String status, List<String> images, String sellerNickname, User seller) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = Status.valueOf(status);
        this.productImages = mapProductImages(images);
        this.seller = seller;
        this.seller.setNickname(sellerNickname);
        this.category = new Category();
        this.isDeleted = false;
    }

    private List mapProductImages(List<String> images) {
        return images.stream()
                .map(imageUrl -> new ProductImage(null, this, imageUrl))
                .collect(Collectors.toList());
    }

    // DTO 기반 업데이트 메서드
    public void updateFromDTO(ProductUpdateDTO request, Category category, User seller) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.category = category;
        this.seller = seller;

        // 이미지 리스트 처리 (기존 이미지 삭제 후 새로 추가)
        this.productImages.clear(); // 기존 이미지 제거
        if (request.getImages() != null) {
            for (String imageUrl : request.getImages()) {
                this.productImages.add(
                        ProductImage.builder()
                                .product(this)
                                .productImageUrl(imageUrl)
                                .build()
                );
            }
        }
    }
}


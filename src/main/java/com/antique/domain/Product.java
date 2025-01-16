package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column
    private String productImage;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Dibs> dibs;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SalesProduct> salesProducts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

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
        AVAILABLE, SOLD_OUT, DELETED
    }

    public Product(Long productId, String name, String description, int price, String status, List<String> images, String sellerNickname) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = Status.valueOf(status);
        this.productImages = images.stream()
                .map(imageUrl -> new ProductImage(null, this, imageUrl)) // ProductImage 생성자에 맞게 수정
                .collect(Collectors.toList());
        this.seller = new User();
        this.seller.setNickname(sellerNickname);
        this.category = new Category();
        this.isDeleted = false;
    }
}


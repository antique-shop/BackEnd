package com.antique.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    private String address;

    private String nickname;

    @Column(nullable = false)
    private String password;

    private String grade; // 등급

    private int points; // 적립금

    private int transactionCount; // 거래 횟수

    private float rating; // 평점

    @Enumerated(EnumType.STRING)
    private Role role; // ENUM 타입 (예: ADMIN, USER 등)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted; // 삭제 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Coupon> coupons;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Dibs> dibs;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    // Enum 정의
    public enum Role {
        ADMIN, USER
    }
}


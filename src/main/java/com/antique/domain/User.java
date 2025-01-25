package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String grade; // 등급

    private int points; // 적립금

    private int transactionCount; // 거래 횟수
    // 판매할 때도 늘어나고, 구매할 때도 늘어난다.

    private float rating; // 평점

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isDeleted; // 삭제 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Dibs> dibs;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    public void updateNickname(String nickname)
    {
        this.nickname = nickname;
    }
}


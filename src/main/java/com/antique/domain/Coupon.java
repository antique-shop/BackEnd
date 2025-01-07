package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

    private String name;

    private String content;

    private int deductedPrice;
}


package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseDetailId;

    @ManyToOne
    @JoinColumn(name = "purchaseId")
    @ToString.Exclude
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        ONGOING, COMPLETED, AVAILABLE
    }
    // 거래 중, 거래 완료, 판매 중(거래 가능)
}


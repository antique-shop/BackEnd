package com.antique.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sales_product")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesDetailId;

    @ManyToOne
    @JoinColumn(name = "saleId", nullable = false)
    @ToString.Exclude
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private int salesPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        AVAILABLE, SOLD_OUT
    }
    // 판매 중, 판매 완료
}


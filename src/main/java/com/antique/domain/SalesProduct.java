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
    private Long salesProductId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    @Column
    private int salesPrice;

    @Enumerated(EnumType.STRING)
    @Column
    private SalesProduct.Status status;

    public enum Status {
        ONGOING, COMPLETED, AVAILABLE
    }
    //  거래 중(예약 중), 거래 완료, 판매 중(거래 가능)
}


package com.antique.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @ToString.Exclude
    private Product product;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PurchaseProduct> purchaseProducts;

    @Column(nullable = false)
    private LocalDate orderDate;
}

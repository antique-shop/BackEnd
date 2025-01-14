package com.antique.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dibs")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Dibs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dibsId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @ToString.Exclude
    private Product product;
}


package com.antique.repository;


import com.antique.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // 특정 사용자의 구매 목록 조회
    List<Purchase> findByUser_UserId(Long userId);

    // 특정 상품의 구매 내역 조회
    List<Purchase> findByProduct_ProductId(Long productId);
}


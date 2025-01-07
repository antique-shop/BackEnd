package com.antique.repository;

import com.antique.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 판매자 ID로 상품 목록 조회
    List<Product> findBySeller_UserId(Long sellerId);

}


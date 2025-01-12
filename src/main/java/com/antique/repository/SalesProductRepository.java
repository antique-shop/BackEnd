package com.antique.repository;

import com.antique.domain.SalesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesProductRepository extends JpaRepository<SalesProduct, Long> {

    // 특정 사용자의 판매 내역 조회
    List<SalesProduct> findByUser_UserId(Long userId);
}

package com.antique.repository;

import com.antique.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // 특정 사용자의 판매 내역 조회
    List<Sale> findByUser_UserId(Long userId);
}

package com.antique.repository;

import com.antique.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // 특정 사용자의 쿠폰 목록 조회
    List<Coupon> findByUser_UserId(Long userId);
}

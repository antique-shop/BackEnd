package com.antique.repository;

import com.antique.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 상품의 리뷰 목록 조회
    List<Review> findByProduct_ProductId(Long productId);

    // 특정 사용자가 작성한 리뷰 조회
    List<Review> findByUser_UserId(Long userId);
}

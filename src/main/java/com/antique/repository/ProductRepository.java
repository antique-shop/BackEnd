package com.antique.repository;

import com.antique.domain.Product;
import com.antique.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 판매자 ID로 상품 목록 조회
    List<Product> findBySeller_UserId(Long sellerId);

    // 카테고리ID로 상품 목록 조회
    List<Product> findByCategory_CategoryId(Long categoryId);

    // status가 available이고, isdeleted 필드가 false이며 userid로 product 엔티티를 찾는 함수
    List<Product> findBySellerAndStatusAndIsDeleted(User seller, Product.Status status, Boolean isDeleted);
}


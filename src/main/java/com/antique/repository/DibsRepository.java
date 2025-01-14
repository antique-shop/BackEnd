package com.antique.repository;

import com.antique.domain.Dibs;
import com.antique.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DibsRepository extends JpaRepository<Dibs, Long> {
    // 특정 사용자의 찜 목록 조회
    List<Dibs> findByUser(User user);
}

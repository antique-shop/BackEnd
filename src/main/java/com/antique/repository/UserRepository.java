package com.antique.repository;

import com.antique.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.rating = :rating WHERE u.userId = :userId")
    void updateUserRating(Long userId, double rating);

    boolean existsByNickname(String nickname);
}
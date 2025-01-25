package com.antique.service.review;

import com.antique.TestDataFactory;
import com.antique.domain.Product;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.repository.ReviewRepository;
import com.antique.service.ReviewService;
import com.antique.domain.Review;
import com.antique.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService; // getUserReviews 메서드가 포함된 서비스 클래스

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserReviews_Success() {
        // Given
        Long userId = 1L;
        Long reviewedId = 2L;

        User reviewer = TestDataFactory.createUser(1L, "reviewer@example.com", "reviewerNickname");
        User reviewedUser = TestDataFactory.createUser(2L, "reviewed@example.com", "reviewedNickname");
        Product product = TestDataFactory.createProduct(1L, "Product1", "Old Description", 1000);

        Review review = TestDataFactory.createReview(product, reviewer, reviewedUser,"Great product!", LocalDateTime.parse("2025-01-23T00:00:00"), 5);

        when(reviewRepository.findByReviewedUser_UserId(reviewedId)).thenReturn(List.of(review));

        // When
        List<GetUserReviewDTO> result = reviewService.getUserReviews(reviewedId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getProductName());
        assertEquals("Great product!", result.get(0).getContent());
        assertEquals(LocalDateTime.parse("2025-01-23T00:00:00"), result.get(0).getReviewDate());
        assertEquals(5, result.get(0).getRating());
    }
}
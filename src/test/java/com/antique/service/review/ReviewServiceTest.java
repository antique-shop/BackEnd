package com.antique.service.review;

import com.antique.TestDataFactory;
import com.antique.domain.Product;
import com.antique.dto.review.ReviewRequestDTO;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.exception.review.ReviewNotFoundException;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.ProductRepository;
import com.antique.repository.ReviewRepository;
import com.antique.domain.Review;
import com.antique.domain.User;
import com.antique.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService; // getUserReviews 메서드가 포함된 서비스 클래스

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * 리뷰 조회
     */
    @Test
    public void testGetUserReviews_Success() {
        // Given
        Long reviewedId = 2L;

        User reviewer = TestDataFactory.createUser(1L, "reviewer@example.com", "reviewerNickname", "reviewerAddress");
        User reviewedUser = TestDataFactory.createUser(2L, "reviewed@example.com", "reviewedNickname", "reviewerAddress");
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

    /*
     * 리뷰 작성
     */
    @Test
    public void testCreateReview_Success() {
        // Given
        ReviewRequestDTO reviewRequest = new ReviewRequestDTO(1L, 2L, 1L, 5, "Great product!");
        User reviewer = TestDataFactory.createUser(1L, "reviewer@example.com", "reviewerNickname", "reviewerAddress");
        User reviewedUser = TestDataFactory.createUser(2L, "reviewed@example.com", "reviewedNickname", "reviewerAddress");
        Product product = TestDataFactory.createProduct(1L, "Product1", "Old Description", 1000);

        when(userRepository.findById(1L)).thenReturn(Optional.of(reviewer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(reviewedUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Review result = reviewService.createReview(reviewRequest);

        // Then
        assertNotNull(result);
        assertEquals("Great product!", result.getContent());
        assertEquals(5, result.getRating());
        assertEquals(reviewer, result.getReviewer());
        assertEquals(reviewedUser, result.getReviewedUser());
        assertEquals(product, result.getProduct());
        assertNotNull(result.getReviewDate());
    }

    /*
     * 리뷰 수정
     */
    @Test
    public void testUpdateReview_Success() {
        // Given
        Long reviewId = 1L;

        User reviewer = TestDataFactory.createUser(1L, "reviewer@example.com", "reviewerNickname", "reviewerAddress");
        User reviewedUser = TestDataFactory.createUser(2L, "reviewed@example.com", "reviewedNickname", "reviewerAddress");
        Product product = TestDataFactory.createProduct(1L, "Product1", "Old Description", 1000);

        Review existingReview = TestDataFactory.createReview(product, reviewer, reviewedUser,"Great product!", LocalDateTime.parse("2025-01-23T00:00:00"), 5);

        ReviewRequestDTO reviewRequest = new ReviewRequestDTO();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Review result = reviewService.updateReview(reviewId, reviewRequest);

        // Then
        assertNotNull(result);
        assertEquals(reviewRequest.getContent(), result.getContent());
        assertEquals(reviewRequest.getRating(), result.getRating());
        assertNotNull(result.getReviewDate()); // 날짜 현재로 업데이트됨
    }

    /*
     * 리뷰 삭제
     */
    @Test
    public void testDeleteReview_Success() {
        // Given
        Long reviewId = 1L;
        User reviewer = TestDataFactory.createUser(1L, "reviewer@example.com", "reviewerNickname", "reviewerAddress");
        User reviewedUser = TestDataFactory.createUser(2L, "reviewed@example.com", "reviewedNickname", "reviewerAddress");
        Product product = TestDataFactory.createProduct(1L, "Product1", "Old Description", 1000);

        Review existingReview = TestDataFactory.createReview(product, reviewer, reviewedUser,"Great product!", LocalDateTime.parse("2025-01-23T00:00:00"), 5);


        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        // When
        reviewService.deleteReview(reviewId);

        // Then
        verify(reviewRepository, times(1)).delete(existingReview);
    }

    @Test
    public void testCreateReview_UserNotFound() {
        // Given
        ReviewRequestDTO reviewRequest = new ReviewRequestDTO(1L, 2L, 1L, 5, "Great product!");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> reviewService.createReview(reviewRequest));
    }

    @Test
    public void testUpdateReview_ReviewNotFound() {
        // Given
        Long reviewId = 1L;
        ReviewRequestDTO reviewRequest = new ReviewRequestDTO(/* parameters for update */);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview(reviewId, reviewRequest));
    }


    @Test
    public void testDeleteReview_ReviewNotFound() {
        // Given
        Long reviewId = 1L;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(reviewId));
    }
}
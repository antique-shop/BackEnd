package com.antique.service;

import com.antique.domain.Product;
import com.antique.domain.Review;
import com.antique.domain.User;
import com.antique.dto.review.ReviewRequestDTO;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.exception.review.ReviewErrorCode;
import com.antique.exception.review.ReviewNotFoundException;
import com.antique.exception.user.UserErrorCode;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.ProductRepository;
import com.antique.repository.ReviewRepository;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /*
    * 특정 사용자 리뷰 조회
    */
    public List<GetUserReviewDTO> getUserReviews(Long userId) {
        List<Review> reviews = reviewRepository.findByReviewedUser_UserId(userId);

        // 리뷰가 존재하지 않을 경우 예외 처리
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        return reviews.stream()
                .map(review -> new GetUserReviewDTO(
                        review.getProduct().getName(),
                        review.getReviewer().getNickname(),
                        review.getContent(),
                        review.getReviewDate(),
                        review.getRating()
                ))
                .collect(Collectors.toList());
    }

    /*
    * 리뷰 작성
    */
    public Review createReview(ReviewRequestDTO reviewRequest) {
        User reviewer = userRepository.findById(reviewRequest.getReviewerId())
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        User reviewedUser = userRepository.findById(reviewRequest.getReviewedUserId())
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Review review = new Review.Builder()
                .reviewer(reviewer)
                .reviewedUser(reviewedUser)
                .product(product)
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .reviewDate(LocalDateTime.now())
                .build();
        return reviewRepository.save(review);
    }
}

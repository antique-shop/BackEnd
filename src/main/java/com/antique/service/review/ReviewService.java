package com.antique.service.review;

import com.antique.domain.Product;
import com.antique.domain.Review;
import com.antique.dto.review.ReviewRequestDTO;
import com.antique.domain.User;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.repository.ProductRepository;
import com.antique.repository.ReviewRepository;
import com.antique.repository.UserRepository;
import jakarta.transaction.Transactional;
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
            throw new BaseException(CommonErrorCode.REVIEW_NOT_FOUND);
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
    @Transactional
    public Review createReview(ReviewRequestDTO reviewRequest) {
        User reviewer = userRepository.findById(reviewRequest.getReviewerId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        User reviewedUser = userRepository.findById(reviewRequest.getReviewedUserId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));

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

    /*
    * 리뷰 수정
    */
    @Transactional
    public Review updateReview(Long reviewId, ReviewRequestDTO reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(ReviewErrorCode.REVIEW_IS_NOT_EXIST));

        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
        review.setReviewDate(LocalDateTime.now()); // 수정 시에도 날짜를 현재로 업데이트

        return reviewRepository.save(review);
    }

    /*
    * 리뷰 삭제
    */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(ReviewErrorCode.REVIEW_IS_NOT_EXIST));

        reviewRepository.delete(review);
    }
}

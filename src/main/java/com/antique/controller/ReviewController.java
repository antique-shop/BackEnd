package com.antique.controller;

import com.antique.domain.Review;
import com.antique.dto.GenericResponseDTO;
import com.antique.dto.review.ReviewRequestDTO;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.service.ProductService;
import com.antique.service.ReviewService;
import com.antique.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "review API", description = "리뷰와 관련된 API 목록 입니다.")
public class ReviewController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final ProductService productService;

    /*
    * 특정 사용자 리뷰 조회
    */
    @Operation(summary = "특정 사용자 리뷰 조회 API", description = "특정 사용자의 리뷰를 조회하는 API입니다.")
    @GetMapping("/getUserReviews")
    public List<GetUserReviewDTO> getUserReviews(
            @Parameter(description="리뷰를 조회하고 싶은 사용자의 ID", required = true)
            @RequestParam Long userId) {
        return reviewService.getUserReviews(userId);
    }

    /*
    * 리뷰 작성
    */
    @Operation(summary = "리뷰 작성 API", description = "리뷰를 작성하는 API입니다.")
    @PostMapping("/postReview")
    public ResponseEntity<GenericResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);

        Long reviewId = review.getReviewId();

        GenericResponseDTO responseDto = new GenericResponseDTO(
                reviewId,
                "리뷰가 성공적으로 등록되었습니다.",
                HttpStatus.OK.value() // HTTP 상태 코드
        );

        return ResponseEntity.ok(responseDto);
    }
}

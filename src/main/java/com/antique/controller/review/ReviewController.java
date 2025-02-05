package com.antique.controller.review;

import com.antique.domain.Review;
import com.antique.dto.review.ReviewRequestDTO;
import com.antique.dto.user.GetUserReviewDTO;
import com.antique.service.jwt.JwtTokenGenerator;
import com.antique.service.review.ReviewService;
import com.antique.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.antique.dto.GenericResponseDTO;


import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "review API", description = "리뷰와 관련된 API 목록 입니다.")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtTokenGenerator jwtTokenGenerator;

    /*
    * 특정 사용자 리뷰 조회
    */
    @Operation(summary = "특정 사용자 리뷰 조회 API", description = "특정 사용자의 리뷰를 조회하는 API입니다.")
    @GetMapping("/getUserReviews")
    public List<GetUserReviewDTO> getUserReviews(
            @Parameter(description = "JWT Access Token", required = true)
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

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


    /*
     * 리뷰 수정
     */
    @Operation(summary = "리뷰 수정 API", description = "특정 리뷰를 수정하는 API입니다.")
    @PutMapping("/updateReview")
    public ResponseEntity<GenericResponseDTO> updateReview(
            @Parameter(description = "수정할 리뷰의 ID", required = true)
            @RequestParam Long reviewId,
            @RequestBody ReviewRequestDTO reviewRequest) {
        Review updatedReview = reviewService.updateReview(reviewId, reviewRequest);

        GenericResponseDTO responseDto = new GenericResponseDTO(
                reviewId,
                "리뷰가 성공적으로 수정되었습니다.",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(responseDto);
    }

    /*
     * 리뷰 삭제
     */
    @Operation(summary = "리뷰 삭제 API", description = "특정 리뷰를 삭제하는 API입니다.")
    @DeleteMapping("/deleteReview")
    public ResponseEntity<GenericResponseDTO> deleteReview(
            @Parameter(description = "삭제할 리뷰의 ID", required = true)
            @RequestParam Long reviewId) {
        reviewService.deleteReview(reviewId);

        GenericResponseDTO responseDto = new GenericResponseDTO(
                reviewId,
                "리뷰가 성공적으로 삭제되었습니다.",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(responseDto);
    }
}

package com.antique.controller;

import com.antique.dto.user.GetUserReviewDTO;
import com.antique.service.ReviewService;
import com.antique.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "review API", description = "리뷰와 관련된 API 목록 입니다.")
public class ReviewController {
    private final UserService userService;
    private final ReviewService reviewService;

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
}

package com.antique.controller.user;

import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductResponseDTO;
import com.antique.service.user.UserProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "유저 상품 API", description = "사용자의 상품 등록과 관련된 API입니다.")
public class UserProductController {

    private final UserProductService userProductService;

    @Operation(summary = "상품 등록", description = "사용자가 새로운 상품을 등록하는 API입니다.")
    @PostMapping("/register")
    public ResponseEntity<ProductResponseDTO> registerProduct(@RequestBody ProductRequestDTO request) {
        Long productId = userProductService.registerProduct(request);
        // 응답 DTO 생성
        ProductResponseDTO response = new ProductResponseDTO(
                productId,
                "상품이 성공적으로 등록되었습니다.",
                200 // HTTP 상태 코드
        );

        // 응답 반환
        return ResponseEntity.ok(response);
    }
}
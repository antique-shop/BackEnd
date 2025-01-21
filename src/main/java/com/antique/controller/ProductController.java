package com.antique.controller;

import com.antique.dto.product.ProductDTO;
import com.antique.dto.product.ProductInfoDTO;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductResponseDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "상품 API", description = "상품과 관련된 API 목록 입니다.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "사용자가 새로운 상품을 등록하는 API입니다.")
    @PostMapping("/register")
    public ResponseEntity<ProductResponseDTO> registerProduct(@RequestBody ProductRequestDTO request) {
        Long productId = productService.registerProduct(request);
        // 응답 DTO 생성
        ProductResponseDTO response = new ProductResponseDTO(
                productId,
                "상품이 성공적으로 등록되었습니다.",
                HttpStatus.OK.value() // HTTP 상태 코드
        );

        // 응답 반환
        return ResponseEntity.ok(response);
    }

    /*
    상품 수정
    */
    @Operation(summary = "상품 수정", description = "사용자가 상품 정보를 수정하는 API입니다.")
    @PostMapping("/update")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody ProductUpdateDTO request) {
        Long updatedProductId = productService.updateProduct(request);
        ProductResponseDTO response = new ProductResponseDTO(
                updatedProductId,
                "상품이 성공적으로 수정되었습니다.",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 삭제", description = "등록된 상품을 삭제하는 API입니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<ProductResponseDTO> deleteProduct(
            @Parameter(description = "삭제할 상품의 ID", required = true)
            @RequestParam Long productId) {
        ProductResponseDTO response = new ProductResponseDTO(
                productId,
                "상품이 성공적으로 삭제되었습니다.",
                HttpStatus.OK.value()
        );
        productService.deleteProduct(productId);
        return ResponseEntity.ok(response);
    }

    /*
    상품 전체 목록 조회
     */
    @Operation(summary = "상품 전체 목록 조회", description = "상품의 전체 목록을 조회하는 API 입니다.")
    @GetMapping("/getProducts")
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    /*
    상품 카테고리별 목록 조회
     */
    @Operation(summary = "상품 카테고리별 목록 조회", description = "상품의 카테고리별 목록을 조회하는 API 입니다.")
    @GetMapping("/getProductsByCategory")
    public List<ProductDTO> getProductsByCategory(
            @Parameter(description="조회할 카테고리 ID", required = true)
            @RequestParam Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /*
    상품 상세 보기
    */
    @Operation(summary = "상품 상세 보기", description = "상품의 상세 정보를 조회하는 API 입니다.")
    @GetMapping("/getProductInfo")
    public ResponseEntity<ProductInfoDTO> getProductInfo(
            @Parameter(name = "productId", description = "상품 ID, query string")
            @RequestParam Long productId) {
        return productService.getProductInfo(productId);
    }

}

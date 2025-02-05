package com.antique.controller.product;

import com.antique.dto.product.ProductGetDTO;
import com.antique.dto.product.ProductDTO;
import com.antique.dto.product.ProductInfoDTO;

import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductResponseDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.service.jwt.JwtTokenGenerator;
import com.antique.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "상품 API", description = "상품과 관련된 API 목록 입니다.")
public class ProductController {

    private final ProductService productService;
    private final JwtTokenGenerator jwtTokenGenerator;

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

    @Operation(summary = "사용자가 판매 중인 상품 조회", description = "사용자가 판매 중인 상품 목록을 조회하는 API입니다.")
    @GetMapping("/getByUserId")
    public ResponseEntity<List<ProductGetDTO>> getProductsByUserId(
            @Parameter(description = "JWT Access Token", required = true)
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출
        List<ProductGetDTO> products = productService.getProductByUserId(userId);
        return ResponseEntity.ok(products);
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

    /*
    상품명으로 상품 검색
    */
    @Operation(summary = "상품명으로 상품 검색", description = "상품명으로 상품을 검색하는 API 입니다.")
    @GetMapping("/searchByProductName")
    public ResponseEntity<List<ProductDTO>> searchByProductName(
            @Parameter(description = "JWT Access Token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(name = "productName", description = "검색하고자 하는 상품명, query string")
            @RequestParam String productName) {

        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

        // 최근 검색어 저장
        productService.saveRecentSearch(userId, productName);

        // 상품 검색
        List<ProductDTO> products = productService.searchByProductName(productName);

        return ResponseEntity.ok(products);
    }

    /*
    최근 검색어 조회
    */
    @Operation(summary = "최근 검색어 조회", description = "최근 검색어를 조회하는 API 입니다.")
    @GetMapping("/getRecentSearches")
    public ResponseEntity<List<String>> getRecentSearches(
            @Parameter(description = "JWT Access Token", required = true)
            @RequestHeader("Authorization") String token) {

        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

        List<String> recentSearches = productService.getRecentSearches(userId);

        // 중복 제거
        List<String> uniqueSortedSearches = recentSearches.stream()
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(uniqueSortedSearches);
    }
}

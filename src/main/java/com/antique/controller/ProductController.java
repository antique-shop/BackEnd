package com.antique.controller;

import com.antique.dto.ProductDTO;
import com.antique.service.ProductService;
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
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "상품 API", description = "상품과 관련된 API 목록 입니다.")
public class ProductController {

    private final ProductService productService;

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
}

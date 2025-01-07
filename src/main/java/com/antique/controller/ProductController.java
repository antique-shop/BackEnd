package com.antique.controller;

import com.antique.domain.Product;
import com.antique.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*
    상품 전체 목록 조회
     */
    @GetMapping("/getProducts")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}

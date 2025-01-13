package com.antique.service;

import com.antique.domain.Product;
import com.antique.dto.ProductDTO;
import com.antique.dto.ProductInfoDTO;
import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*
    상품 전체 목록 조회
    */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDTO(products);
    }

    /*
    상품 카테고리별 목록 조회
    */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        return convertToProductDTO(products);
    }

    /*
    상품 상세 보기
    */
    public ResponseEntity<ProductInfoDTO> getProductInfo(Long productId) {
        return productRepository.findById(productId)
                .map(product -> ResponseEntity.ok(new ProductInfoDTO(product)))
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    private List<ProductDTO> convertToProductDTO(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus().toString(),
                        product.getProductImage(),
                        product.getSeller().getNickname()
                ))
                .collect(Collectors.toList());
    }
}

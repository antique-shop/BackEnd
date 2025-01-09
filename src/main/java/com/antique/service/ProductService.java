package com.antique.service;

import com.antique.domain.Product;
import com.antique.dto.ProductDTO;
import com.antique.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

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

    /*
    상품 카테고리별 목록 조회
    */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);

        return products.stream()
                .map(product -> new ProductDTO (
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

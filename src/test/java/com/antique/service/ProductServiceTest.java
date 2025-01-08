package com.antique.service;

import com.antique.domain.Product;
import com.antique.dto.ProductDTO;
import com.antique.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static class TestProduct extends Product {
        public TestProduct(Long productId, String name, String description, int price, String status, String image, String sellerNickname) {
            super(productId, name, description, price, status, image, sellerNickname);
        }
    }

    @Test
    public void testGetAllProducts() {
        // Given
        Product product1 = new TestProduct(1L, "Product 1", "Description 1", 100, "AVAILABLE", "없음", "nickname1");
        Product product2 = new TestProduct(2L, "Product 2", "Description 2", 200, "SOLD_OUT", "없음", "nickname1");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    /*
    * 상품이 존재하지 않을 때
    * */
    @Test
    public void testGetAllProducts_WhenNoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(0, result.size());
    }
}
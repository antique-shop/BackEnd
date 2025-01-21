package com.antique.service;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.ProductDTO;
import com.antique.dto.ProductInfoDTO;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService; // 테스트할 객체

    @Mock
    private ProductRepository productRepository; // Mock 객체

    private static class TestCategory extends Category {
        public TestCategory(CategoryName categoryName) {
            super(categoryName);
        }
    }


    /* [ 상품 전체 목록 조회 ]
    * - 상품이 존재하는 경우
    */

    @Test
    public void testGetAllProducts() {

        // given
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");
        Product product1 = TestDataFactory.createProduct(1L, "Product 1", "Description 1", 100);
        Product product2 = TestDataFactory.createProduct(2L, "Product 2", "Description 2", 200);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // when
        List<ProductDTO> result = productService.getAllProducts();

        // then
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    /* [ 상품 전체 목록 조회 ]
    * - 상품이 존재하지 않는 경우
    */

    @Test
    public void testGetAllProducts_WhenNoProducts() {

        // given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ProductDTO> result = productService.getAllProducts();

        // then
        assertEquals(0, result.size());
    }

     /* [ 상품 카테고리별 목록 조회 ]
     */

    @Test
    public void testGetProductsByCategory() {
        // Given
        Long categoryId = 1L;
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");
        Product product1 = TestDataFactory.createProduct(1L, "Product 1", "Description 1", 100);
        Product product2 = TestDataFactory.createProduct(2L, "Product 2", "Description 2", 200);

        when(productRepository.findByCategory_CategoryId(categoryId)).thenReturn(Arrays.asList(product1, product2));

        // When
        List<ProductDTO> result = productService.getProductsByCategory(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        assertEquals("nickname1", result.get(0).getSellerNickName());
        assertEquals("nickname2", result.get(1).getSellerNickName());
    }

     /* [ 상품 상세 정보 조회 ]
     * - 상품이 존재하는 경우
     * - 상품이 존재하지 않는 경우
     */

    @Test
    public void testGetProductInfo() {
        // Given
        Long productId = 1L;
        Long sellerUserId = 1L;
        Long categoryId = 1L;

        // 상품 이미지
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");

        // 상품 카테고리
        Category category = TestDataFactory.createCategory(categoryId, Category.CategoryName.TOPS);

        // User 객체 생성
        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");

        Product product = TestDataFactory.createProduct(1L, "Product 1", "Description 1", 100);

        // 상품이 존재하는 경우
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        ResponseEntity<ProductInfoDTO> response = productService.getProductInfo(productId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        ProductInfoDTO productInfo = response.getBody();
        assertNotNull(productInfo);
        assertEquals(productId, productInfo.getProductId());
        assertEquals("Product 1", productInfo.getProductName());
        assertEquals("Description 1", productInfo.getDescription());
        assertEquals(productImages, productInfo.getProductImages());
        assertEquals(100, productInfo.getPrice());
        assertEquals("SellerNickname", productInfo.getSellerNickname());
        assertEquals(4.0, productInfo.getSellerRating());

        // 상품이 존재하지 않는 경우
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.getProductInfo(productId));
    }

}
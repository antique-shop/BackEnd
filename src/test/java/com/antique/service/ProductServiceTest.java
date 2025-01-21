package com.antique.service;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.ProductDTO;
import com.antique.dto.ProductInfoDTO;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private User seller;
    private Category category;
    private Product product;

    @BeforeEach
    public void setUp() {
        Long sellerUserId = 1L;
        Long categoryId = 1L;
        Long productId = 1L;

        seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");
        category = TestDataFactory.createCategory(categoryId, Category.CategoryName.TOPS);
        product = TestDataFactory.createProductWithDefaults(productId, seller, category);
    }

    /*
     * 상품 전체 목록 조회
     */
    @Test
    public void testGetAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of(product));

        // When
        List<ProductDTO> productDTOs = productService.getAllProducts();

        // Then
        assertNotNull(productDTOs);
        assertEquals(1, productDTOs.size());
        assertEquals(product.getProductId(), productDTOs.get(0).getProductId());
    }

    /*
     * 상품 카테고리별 목록 조회
     */
    @Test
    public void testGetProductsByCategory() {
        // Given
        Long categoryId = category.getCategoryId();
        when(productRepository.findByCategory_CategoryId(categoryId)).thenReturn(List.of(product));

        // When
        List<ProductDTO> productDTOs = productService.getProductsByCategory(categoryId);

        // Then
        assertNotNull(productDTOs);
        assertEquals(1, productDTOs.size());
        assertEquals(product.getProductId(), productDTOs.get(0).getProductId());
    }


    /*
    * 상품 상세 정보 조회
    * - 해당 상품이 있는 경우
    */
    @Test
    public void testGetProductInfo_ProductExists() {
        // Given
        Long productId = product.getProductId();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        ResponseEntity<ProductInfoDTO> response = productService.getProductInfo(productId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        ProductInfoDTO productInfo = response.getBody();
        assertNotNull(productInfo);
        assertEquals(productId, productInfo.getProductId());
        assertEquals("Default Product Name", productInfo.getProductName());
        assertEquals("Default Description", productInfo.getDescription());
        assertEquals(100000, productInfo.getPrice());
        assertEquals("SellerNickname", productInfo.getSellerNickname());
    }

    /*
     * 상품 상세 정보 조회
     * - 해당 상품이 없는 경우
     */
    @Test
    public void testGetProductInfo_ProductDoesNotExist() {
        // Given
        Long productId = product.getProductId();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.getProductInfo(productId));
    }
}
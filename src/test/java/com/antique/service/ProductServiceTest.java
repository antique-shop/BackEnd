package com.antique.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService; // 테스트할 객체

    @Mock
    private ProductRepository productRepository; // Mock 객체

    /*
    * Product 클래스의 생성자가 protected로 설정되어 있어, 해당 클래스의 인스턴스를 테스트 클래스에서 직접 생성할 수 없음.
    * 따라서, Product 객체를 생성하기 위해 Product 클래스의 서브 클래스를 만들었음.
    * */
    private static class TestProduct extends Product {
        public TestProduct(Long productId, String name, String description, int price, String status, List<String> images, String sellerNickname) {
            super(productId, name, description, price, status, images, sellerNickname);
        }
    }

    private static class TestProduct2 extends Product {
        public TestProduct2(Long productId, String name, String description, int price, List<String> images, String sellerNickname, Category category, float sellerRating, User seller) {
            super(productId, name, description, category, price, images, sellerNickname, sellerRating, seller);
        }
    }

    private static class TestCategory extends Category {
        public TestCategory(CategoryName categoryName) {
            super(categoryName);
        }
    }


    /*
    * [ 상품 전체 목록 조회 ]
    * - 상품이 존재하는 경우
    * */
    @Test
    public void testGetAllProducts() {

        // given
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");
        Product product1 = new TestProduct(1L, "Product 1", "Description 1", 100, "AVAILABLE", productImages, "nickname1");
        Product product2 = new TestProduct(2L, "Product 2", "Description 2", 200, "SOLD_OUT", productImages, "nickname1");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // when
        List<ProductDTO> result = productService.getAllProducts();

        // then
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    /*
    * [ 상품 전체 목록 조회 ]
    * - 상품이 존재하지 않는 경우
    * */
    @Test
    public void testGetAllProducts_WhenNoProducts() {

        // given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ProductDTO> result = productService.getAllProducts();

        // then
        assertEquals(0, result.size());
    }

    /*
     * [ 상품 카테고리별 목록 조회 ]
     * */
    @Test
    public void testGetProductsByCategory() {
        // Given
        Long categoryId = 1L;
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");
        Product product1 = new Product(1L, "Product 1", "Description 1", 100, "AVAILABLE", productImages, "nickname1");
        Product product2 = new Product(2L, "Product 2", "Description 2", 200, "SOLD_OUT", productImages, "nickname2");

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

    /*
     * [ 상품 상세 정보 조회 ]
     * - 상품이 존재하는 경우
     * - 상품이 존재하지 않는 경우
     * */
    @Test
    public void testGetProductInfo() {
        // Given
        Long productId = 1L;

        // 상품 이미지
        List<String> productImages = Arrays.asList("image1.jpg", "image2.jpg");

        // 상품 카테고리
        TestCategory category = new TestCategory(Category.CategoryName.TOPS);
        category.setCategoryName(Category.CategoryName.TOPS);

        // User 객체 생성
        User seller = User.createUser("nickname1");
        seller.setNickname("nickname1");

        Product product = new TestProduct2(productId, "Product 1", "Description 1", 100, productImages, "nickname1", category, 4, seller);

        // 상품이 존재하는 경우
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));

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
        assertEquals("nickname1", productInfo.getSellerNickname());
        assertEquals(4.0, productInfo.getSellerRating());

        // 상품이 존재하지 않는 경우
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.getProductInfo(productId));
    }

}
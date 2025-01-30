package com.antique.service;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.product.ProductDTO;
import com.antique.dto.product.ProductInfoDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.repository.ProductRepository;
import com.antique.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations; // ListOperations Mock 추가

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

        seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname");
        category = TestDataFactory.createCategory(categoryId, Category.CategoryName.TOPS);
        product = TestDataFactory.createProductWithDefaults(productId, seller, category);

        // RedisTemplate의 opsForList()가 ListOperations를 반환하도록 설정
        when(redisTemplate.opsForList()).thenReturn(listOperations);
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

        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);

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

        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);
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

        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);

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
        BaseException exception = assertThrows(
                BaseException.class,
                () -> productService.getProductInfo(productId)
        );

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.PRODUCT_NOT_FOUND);

        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);
    }


    /*
     * 상품명으로 상품 검색
     * - 상품이 있는 경우
     */
    @Test
    public void testSearchByProductName_ProductExists() {
        // Given
        String productName = "Default Product Name";
        when(productRepository.findByNameContaining(productName)).thenReturn(List.of(product));

        // When
        List<ProductDTO> productDTOs = productService.searchByProductName(productName);

        // Then
        assertNotNull(productDTOs);
        assertEquals(1, productDTOs.size());
        assertEquals(product.getProductId(), productDTOs.get(0).getProductId());

        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    /*
     * 상품명으로 상품 검색
     * - 상품이 없는 경우
     */
    @Test
    public void testSearchByProductName_ProductDoesNotExist() {
        // Given
        String productName = "Nonexistent Product Name";
        when(productRepository.findByNameContaining(productName)).thenReturn(List.of());

        // When & Then
        BaseException exception = assertThrows(
                BaseException.class,
                () -> productService.searchByProductName(productName)
        );

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.NO_PRODUCT_BY_SEARCH);

        // lenient()를 사용하여 이 모킹이 실제 테스트에서 사용되지 않더라도 Mockito가 이를 불필요한 모킹으로 간주하지 않게 함
        lenient().when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    /*
    * 최근 검색어 저장
    */
    @Test
    public void testSaveRecentSearch() {
        // Given
        Long userId = 1L;
        String searchTerm = "Test Product";

        // When
        productService.saveRecentSearch(userId, searchTerm);

        // Then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(listOperations).leftPush(eq("recent_searches:" + userId), captor.capture());
        assertEquals(searchTerm, captor.getValue());

        // Verify that the list is trimmed to a maximum of 10 items
        verify(listOperations).trim("recent_searches:" + userId, 0, 9);
    }

    /*
    * 최근 검색어 조회
    */
    @Test
    public void testGetRecentSearches() {
        // Given
        Long userId = 1L;
        List<String> recentSearches = Arrays.asList("Product 1", "Product 2", "Product 3");
        when(listOperations.range("recent_searches:" + userId, 0, -1)).thenReturn(recentSearches);

        // When
        List<String> result = productService.getRecentSearches(userId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(recentSearches, result);
    }
}
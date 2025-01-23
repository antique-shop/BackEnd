package com.antique.service.user;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.ProductImage;
import com.antique.domain.User;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.exception.category.CategoryNotFoundException;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.CategoryRepository;
import com.antique.repository.ProductRepository;
import com.antique.repository.UserRepository;
import com.antique.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testRegisterProduct_Success() {
        // Given: Mock 데이터 설정
        Long productId = 1L;
        Long sellerUserId = 2L;
        Long categoryId = 3L;
        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");
        Category category = new Category(Category.CategoryName.TOPS);
        Product product = TestDataFactory.createProductWithDefaults(productId, seller, category);

        ProductRequestDTO request = TestDataFactory.createProductRequestDTOWithDefaults(sellerUserId, categoryId);

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When: 서비스 호출
        Long savedProductId = productService.registerProduct(request);
        Long productId = productService.registerProduct(request);

        // Then: 결과 검증
        assertThat(productId).isNotNull();
        assertThat(productId).isEqualTo(product.getProductId());

        // 필드 값 검증
        assertThat(product.getName()).isEqualTo(request.getName());
        assertThat(product.getDescription()).isEqualTo(request.getDescription());
        assertThat(product.getPrice()).isEqualTo(request.getPrice());
        assertThat(product.getSeller()).isEqualTo(seller);
        assertThat(product.getCategory()).isEqualTo(category);

        // 이미지 리스트 검증
        assertThat(product.getProductImages()).hasSize(request.getImages().size());
        assertThat(product.getProductImages())
                .extracting(ProductImage::getProductImageUrl)
                .containsExactlyElementsOf(request.getImages());


        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testRegisterProduct_UserNotFound() {
        // Given: 존재하지 않는 판매자 ID
        Long sellerUserId = 999L;
        Long categoryId = 1L;
        ProductRequestDTO request = TestDataFactory.createProductRequestDTOWithDefaults(sellerUserId, categoryId);

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(UserNotFoundException.class, () -> productService.registerProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProduct_CategoryNotFound() {
        // Given: 존재하지 않는 카테고리 ID
        Long sellerUserId = 1L;
        Long categoryId = 999L;
        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");

        ProductRequestDTO request = TestDataFactory.createProductRequestDTOWithDefaults(sellerUserId, categoryId);

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(CategoryNotFoundException.class, () -> productService.registerProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_Success() {
        // Given: Mock 데이터 설정
        Long productId = 1L;
        Long sellerUserId = 2L;
        Long categoryId = 3L;

        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");
        Category category = TestDataFactory.createCategory(categoryId, Category.CategoryName.TOPS);
        Product existingProduct = TestDataFactory.createProduct(productId, "Old Name", "Old Description", 1000);

        ProductUpdateDTO request = TestDataFactory.createProductUpdateDTOWithDefaults(productId, sellerUserId, categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userRepository.findById(sellerUserId)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When: 서비스 호출
        Long updatedProductId = productService.updateProduct(request);

        // Then: 결과 검증
        assertThat(updatedProductId).isEqualTo(productId);

        // 기존 객체에 업데이트된 값이 반영되었는지 검증
        assertThat(existingProduct.getName()).isEqualTo(request.getName());
        assertThat(existingProduct.getDescription()).isEqualTo(request.getDescription());
        assertThat(existingProduct.getPrice()).isEqualTo(request.getPrice());
        assertThat(existingProduct.getCategory()).isEqualTo(category);
        assertThat(existingProduct.getSeller()).isEqualTo(seller);
        assertThat(existingProduct.getProductImages()).hasSize(2);
        assertThat(existingProduct.getProductImages())
                .extracting(ProductImage::getProductImageUrl)
                .containsExactlyElementsOf(request.getImages());

        // save가 한 번 호출되었는지 검증
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Given: 존재하지 않는 상품 ID
        Long productId = 999L;
        Long userId = 2L;
        Long categoryId = 3L;
        ProductUpdateDTO request = TestDataFactory.createProductUpdateDTOWithDefaults(productId, userId, categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_UserNotFound() {
        // Given: 존재하지 않는 판매자 ID
        Long productId = 1L;
        Long sellerUserId = 999L;
        Long categoryId = 3L;

        Product existingProduct = TestDataFactory.createProduct(productId, "Old Name", "Old Description", 1000);
        ProductUpdateDTO request = TestDataFactory.createProductUpdateDTOWithDefaults(productId, sellerUserId, categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userRepository.findById(sellerUserId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(UserNotFoundException.class, () -> productService.updateProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_CategoryNotFound() {
        // Given: 존재하지 않는 카테고리 ID
        Long productId = 1L;
        Long userId = 2L;
        Long categoryId = 999L;

        Product existingProduct = TestDataFactory.createProduct(productId, "Old Name", "Old Description", 1000);
        User seller = TestDataFactory.createUser(2L, "seller@example.com", "SellerNickname", "SellerAddress");
        ProductUpdateDTO request = TestDataFactory.createProductUpdateDTOWithDefaults(productId, userId, categoryId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userRepository.findById(2L)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        // Given: Mock 데이터 설정
        Long productId = 1L;
        Product product = TestDataFactory.createProduct(productId, "Test Product", "Test Description", 1000);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When: 삭제 메서드 호출
       productService.deleteProduct(productId);

        // Then: 검증
        assertThat(product.getIsDeleted()).isTrue(); // isDeleted 필드가 true로 설정되었는지 확인
        verify(productRepository, times(1)).save(product); // 저장 호출 검증
    }
}
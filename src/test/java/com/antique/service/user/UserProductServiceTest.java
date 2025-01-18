package com.antique.service.user;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.exception.category.CategoryNotFoundException;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.CategoryRepository;
import com.antique.repository.ProductRepository;
import com.antique.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProductServiceTest {

    @InjectMocks
    private UserProductService userProductService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testRegisterProduct_Success() {
        // Given: Mock 데이터 설정
        Long sellerUserId = 1L;
        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");
        Category category = new Category(Category.CategoryName.TOPS);

        ProductRequestDTO request = ProductRequestDTO.builder()
                .userId(sellerUserId)
                .name("Antique Shirt")
                .description("A beautiful antique shirt")
                .price(120000)
                .categoryId(1L)
                .images(List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"))
                .build();

        Product expectedProduct = new Product(
                1L,
                request.getName(),
                request.getDescription(),
                category,
                request.getPrice(),
                request.getImages(),
                seller.getNickname(),
                seller.getRating(),
                seller
        );

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // When: 서비스 호출
        Long productId = userProductService.registerProduct(request);

        // Then: 결과 검증
        assertThat(productId).isNotNull();
        assertThat(productId).isEqualTo(expectedProduct.getProductId());


        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testRegisterProduct_UserNotFound() {
        // Given: 존재하지 않는 판매자 ID
        Long sellerUserId = 999L;
        ProductRequestDTO request = ProductRequestDTO.builder()
                .userId(sellerUserId)
                .name("Antique Shirt")
                .description("A beautiful antique shirt")
                .price(120000)
                .categoryId(1L)
                .images(List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"))
                .build();

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(UserNotFoundException.class, () -> userProductService.registerProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testRegisterProduct_CategoryNotFound() {
        // Given: 존재하지 않는 카테고리 ID
        Long sellerUserId = 1L;
        User seller = TestDataFactory.createUser(sellerUserId, "seller@example.com", "SellerNickname", "SellerAddress");

        ProductRequestDTO request = ProductRequestDTO.builder()
                .userId(sellerUserId)
                .name("Antique shirt")
                .description("A beautiful antique shirt")
                .price(120000)
                .categoryId(999L)
                .images(List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"))
                .build();

        when(userRepository.findById(sellerUserId)).thenReturn(Optional.of(seller));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(CategoryNotFoundException.class, () -> userProductService.registerProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }
}
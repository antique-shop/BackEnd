package com.antique.service.user;

import com.antique.TestDataFactory;
import com.antique.domain.Category;
import com.antique.domain.Dibs;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.product.ProductDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.repository.DibsRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDibsServiceTest {

    @InjectMocks
    private UserDibsService userDibsService; // 테스트할 대상

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DibsRepository dibsRepository;

    @Test
    void testAddDibs_Success() {
        // Given: Mock 데이터 생성
        Long userId = 1L;
        Long productId = 101L;
        Long DibsId = 2L;
        Long categoryId = 3L;

        User user = TestDataFactory.createUserWithDefault(userId);
        Category category = TestDataFactory.createCategory(categoryId, Category.CategoryName.TOPS);
        Product product = TestDataFactory.createProductWithDefaults(productId, user, category);
        Dibs savedDibs = Dibs.builder()
                .dibsId(DibsId)
                .user(user)
                .product(product)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(dibsRepository.save(any(Dibs.class))).thenReturn(savedDibs);

        // When: 메서드 호출
        Long resultDibsId = userDibsService.addDibs(userId, productId);

        // Then: 검증
        assertThat(resultDibsId).isEqualTo(DibsId);
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(dibsRepository, times(1)).save(any(Dibs.class));
    }

    @Test
    void testAddDibs_ProductNotFound() {
        // Given: 존재하지 않는 상품 ID
        Long userId = 1L;
        Long productId = 999L;

        User user = TestDataFactory.createUserWithDefault(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        BaseException exception = assertThrows(BaseException.class,
                () -> userDibsService.addDibs(userId, productId));

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.PRODUCT_NOT_FOUND);

        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(dibsRepository, never()).save(any());
    }

    @Test
    void testGetUserDibsProducts_Success() {
        // Given: Mock 데이터 생성
        User user = TestDataFactory.createUserWithDefault(1L);
        Category category = TestDataFactory.createCategory(1L, Category.CategoryName.TOPS);

        Product product1 = TestDataFactory.createProductWithDefaults(101L, user, category);
        Product product2 = TestDataFactory.createProductWithDefaults(102L, user, category);

        Dibs dibs1 = Dibs.builder().user(user).product(product1).build();
        Dibs dibs2 = Dibs.builder().user(user).product(product2).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dibsRepository.findByUser(user)).thenReturn(List.of(dibs1, dibs2));

        // When: 서비스 호출
        List<ProductDTO> result = userDibsService.getUserDibsProducts(1L);

        // Then: 검증
        assertThat(result).hasSize(2);

        ProductDTO productDTO1 = result.get(0);
        assertThat(productDTO1.getProductId()).isEqualTo(101L);
        assertThat(productDTO1.getName()).isEqualTo(product1.getName());
        assertThat(productDTO1.getDescription()).isEqualTo(product1.getDescription());
        assertThat(productDTO1.getPrice()).isEqualTo(product1.getPrice());
        assertThat(productDTO1.getSellerNickName()).isEqualTo(product1.getSeller().getNickname());

        ProductDTO productDTO2 = result.get(1);
        assertThat(productDTO2.getProductId()).isEqualTo(102L);
        assertThat(productDTO2.getName()).isEqualTo(product2.getName());
        assertThat(productDTO2.getDescription()).isEqualTo(product2.getDescription());
        assertThat(productDTO2.getPrice()).isEqualTo(product2.getPrice());
        assertThat(productDTO2.getSellerNickName()).isEqualTo(product2.getSeller().getNickname());

        verify(userRepository, times(1)).findById(1L);
        verify(dibsRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetUserDibsProducts_UserNotFound() {
        // Given: 유저가 존재하지 않는 경우
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        BaseException exception = assertThrows(BaseException.class,
                () -> userDibsService.getUserDibsProducts(999L));

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.USER_NOT_FOUND);

        verify(userRepository, times(1)).findById(999L);
        verify(dibsRepository, never()).findByUser(any());
    }

    @Test
    void testRemoveDibs_Success() {
        // Given: Mock 데이터 생성
        Long userId = 1L;
        Long productId = 101L;

        User user = TestDataFactory.createUserWithDefault(userId);
        Category category = TestDataFactory.createCategory(1L, Category.CategoryName.TOPS);
        Product product = TestDataFactory.createProductWithDefaults(productId, user, category);

        Dibs dibs = Dibs.builder()
                .user(user)
                .product(product)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(dibsRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(dibs));

        // When: 메서드 호출
        userDibsService.removeDibs(userId, productId);

        // Then: 검증
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(dibsRepository, times(1)).findByUserAndProduct(user, product);
        verify(dibsRepository, times(1)).delete(dibs);
    }

    @Test
    void testRemoveDibs_DibsNotFound() {
        // Given: 찜 데이터가 존재하지 않는 경우
        Long userId = 1L;
        Long productId = 101L;

        User user = TestDataFactory.createUserWithDefault(userId);
        Category category = TestDataFactory.createCategory(1L, Category.CategoryName.TOPS);
        Product product = TestDataFactory.createProductWithDefaults(productId, user, category);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(dibsRepository.findByUserAndProduct(user, product)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        BaseException exception = assertThrows(BaseException.class,
                () -> userDibsService.removeDibs(userId, productId));

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.DIBS_NOT_FOUND);

        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(dibsRepository, times(1)).findByUserAndProduct(user, product);
        verify(dibsRepository, never()).delete(any());
    }

}
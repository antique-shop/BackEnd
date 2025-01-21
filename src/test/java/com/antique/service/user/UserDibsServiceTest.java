package com.antique.service.user;

import com.antique.domain.Category;
import com.antique.domain.Dibs;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.product.ProductDTO;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.DibsRepository;
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
    private DibsRepository dibsRepository;

    Category category = new Category(Category.CategoryName.TOPS);

    @Test
    void testGetUserDibsProducts_Success() {
        // Given: Mock 데이터 생성
        User user = User.builder()
                .userId(1L)
                .nickname("TestUser")
                .build();

        User seller1 = User.builder()
                .userId(2L)
                .nickname("Seller1")
                .build();

        User seller2 = User.builder()
                .userId(3L)
                .nickname("Seller2")
                .build();

        Product product1 = new Product(
                101L,
                "Product 1",
                "Description 1",
                category, // 카테고리 추가
                1000, // int 타입 가격
                List.of("image1.jpg"), // 이미지 리스트
                "Seller1", // 판매자 닉네임
                4.5f, // 판매자 평점
                seller1 // 판매자
        );
        product1.setSeller(seller1);

        Product product2 = new Product(
                102L,
                "Product 2",
                "Description 2",
                category, // 카테고리 추가
                2000, // int 타입 가격
                List.of("image2.jpg"), // 이미지 리스트
                "Seller2", // 판매자 닉네임
                4.7f, // 판매자 평점
                seller2 // 판매자
        );
        product2.setSeller(seller2);

        // user가 product1, product2를 찜함
        Dibs dibs1 = Dibs.builder().user(user).product(product1).build();
        Dibs dibs2 = Dibs.builder().user(user).product(product2).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dibsRepository.findByUser(user)).thenReturn(List.of(dibs1, dibs2));

        // When: 서비스 호출
        List<ProductDTO> result = userDibsService.getUserDibsProducts(1L);

        // Then: 검증

        // 찜한 개수가 2개가 나오는지 확인
        assertThat(result).hasSize(2);

        // 찜한 상품 목록이 잘 나오는지 하나하나 확인
        ProductDTO productDTO1 = result.get(0);
        assertThat(productDTO1.getProductId()).isEqualTo(101L);
        assertThat(productDTO1.getName()).isEqualTo("Product 1");
        assertThat(productDTO1.getDescription()).isEqualTo("Description 1");
        assertThat(productDTO1.getPrice()).isEqualTo(1000);
        assertThat(productDTO1.getSellerNickName()).isEqualTo("Seller1");

        ProductDTO productDTO2 = result.get(1);
        assertThat(productDTO2.getProductId()).isEqualTo(102L);
        assertThat(productDTO2.getName()).isEqualTo("Product 2");
        assertThat(productDTO2.getDescription()).isEqualTo("Description 2");
        assertThat(productDTO2.getPrice()).isEqualTo(2000);
        assertThat(productDTO2.getSellerNickName()).isEqualTo("Seller2");

        verify(userRepository, times(1)).findById(1L);
        verify(dibsRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetUserDibsProducts_UserNotFound() {
        // Given: 유저가 존재하지 않는 경우
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        assertThrows(UserNotFoundException.class, () -> userDibsService.getUserDibsProducts(999L));

        verify(userRepository, times(1)).findById(999L);
        verify(dibsRepository, never()).findByUser(any());
    }
}
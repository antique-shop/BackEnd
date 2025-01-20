package com.antique.service.user;

import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.ProductImage;
import com.antique.domain.User;
import com.antique.dto.ProductDTO;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.exception.category.CategoryNotFoundException;
import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.CategoryRepository;
import com.antique.repository.ProductRepository;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long registerProduct(ProductRequestDTO request) {
        // 1. 판매자 확인
        User seller = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        // 2. 카테고리 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        // 3. 상품 생성 (Builder 사용)
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(Product.Status.AVAILABLE)
                .seller(seller)
                .category(category)
                .isDeleted(false)
                .build();

        // 4. 이미지 리스트 처리 및 설정
        List<ProductImage> productImages = Optional.ofNullable(request.getImages())
                .orElse(Collections.emptyList()) // null인 경우 빈 리스트 반환
                .stream()
                .map(imageUrl -> ProductImage.builder()
                        .product(product)
                        .productImageUrl(imageUrl)
                        .build())
                .collect(Collectors.toList());
        product.setProductImages(productImages);

        // 4. 저장
        Product savedProduct = productRepository.save(product);

        // 5. productId만 반환
        return savedProduct.getProductId();
    }

    @Transactional
    public Long updateProduct(ProductUpdateDTO request) {
        // 1. 상품 확인
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));
        // 2. 판매자 확인
        User seller = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);
        // 3. 카테고리 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        // 4. Product 엔티티의 수정 메서드 호출
        product.updateFromDTO(request, category, seller);

        productRepository.save(product);

        return product.getProductId();
    }


}
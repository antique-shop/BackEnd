package com.antique.service;

import com.antique.domain.Category;
import com.antique.domain.Product;
import com.antique.domain.ProductImage;
import com.antique.domain.User;
import com.antique.dto.product.ProductGetDTO;
import com.antique.dto.product.ProductDTO;
import com.antique.dto.product.ProductInfoDTO;
import com.antique.dto.product.ProductRequestDTO;
import com.antique.dto.product.ProductUpdateDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.repository.CategoryRepository;
import com.antique.repository.ProductRepository;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long registerProduct(ProductRequestDTO request) {
        // 1. 판매자 확인
        User seller = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        // 2. 카테고리 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.CATEGORY_NOT_FOUND));

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
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));
        // 2. 판매자 확인
        User seller = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));
        // 3. 카테고리 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BaseException(CommonErrorCode.CATEGORY_NOT_FOUND));
        // 4. Product 엔티티의 수정 메서드 호출
        product.updateFromDTO(request, category, seller);

        productRepository.save(product);

        return product.getProductId();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        // 1. 상품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));

        productRepository.deleteById(productId);
    }

    // user id를 입력받아서 사용자가 판매 중인 물품을 조회하는 코드
    @Transactional(readOnly = true)
    public List<ProductGetDTO> getProductByUserId(Long userId) {
        // 1. 사용자 확인
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        // 2. 판매 중이면서 삭제되지 않은 상품 조회
        List<Product> availableProducts = productRepository.findBySellerAndStatusAndIsDeleted(
                seller, Product.Status.AVAILABLE, false);

        // 3. Product 엔티티를 ProductDTO로 변환하여 반환 (이미지 포함)
        return availableProducts.stream()
                .map(product -> new ProductGetDTO(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus().toString(),
                        product.getProductImages().stream()
                                .map(ProductImage::getProductImageUrl)
                                .collect(Collectors.toList()) // 이미지 URL 리스트
                ))
                .collect(Collectors.toList());
    }
    
    /*
    상품 전체 목록 조회
    */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDTO(products);
    }

    /*
    상품 카테고리별 목록 조회
    */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        return convertToProductDTO(products);
    }

    /*
    상품 상세 정보 조회
    */
    public ResponseEntity<ProductInfoDTO> getProductInfo(Long productId) {
        return productRepository.findById(productId)
                .map(product -> ResponseEntity.ok(new ProductInfoDTO(product)))
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));
    }

    /*
    상품명으로 상품 검색
    */
    public List<ProductDTO> searchByProductName(String productName) {
        List<Product> products = productRepository.findByNameContaining(productName);

        // 상품이 없을 경우 예외 처리
        if (products.isEmpty()) {
            throw new BaseException(CommonErrorCode.NO_PRODUCT_BY_SEARCH);
        }

        return convertToProductDTO(products);
    }


    /*
    응답 dto에 맞게 변환하는 메서드 (상품 전체 목록 조회 / 상품 카테고리별 목록 조회 API에 사용)
    * */
    private List<ProductDTO> convertToProductDTO(List<Product> products) {
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus().toString(),
                        product.getProductImage(),
                        product.getSeller().getNickname()
                ))
                .collect(Collectors.toList());
    }
}

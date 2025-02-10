package com.antique.service.user;

import com.antique.domain.Dibs;
import com.antique.domain.Product;
import com.antique.domain.ProductImage;
import com.antique.domain.User;
import com.antique.dto.dibs.DibsProductDTO;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import com.antique.dto.product.ProductDTO;
import com.antique.repository.DibsRepository;
import com.antique.repository.ProductRepository;
import com.antique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDibsService {

    private final UserRepository userRepository;
    private final DibsRepository dibsRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long addDibs(Long userId, Long productId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));
        // 상품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));

        // 중복 체크
        boolean exists = dibsRepository.existsByUserAndProduct(user, product);
        if (exists) {
            throw new BaseException(CommonErrorCode.DIBS_ALREADY_EXISTS);
        }

        // 찜 데이터 생성
        Dibs dibs = Dibs.builder()
                .user(user)
                .product(product)
                .build();

        Dibs savedDibs = dibsRepository.save(dibs);
        return savedDibs.getDibsId();
    }

    @Transactional
    public void removeDibs(Long userId, Long productId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));
        // 상품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.PRODUCT_NOT_FOUND));

        // 찜 데이터 확인
        Dibs dibs = dibsRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new BaseException(CommonErrorCode.DIBS_NOT_FOUND));

        // 찜 데이터 삭제
        dibsRepository.delete(dibs);
    }


    @Transactional(readOnly = true)
    public List<DibsProductDTO> getUserDibsProducts(Long userId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(CommonErrorCode.USER_NOT_FOUND));

        // 찜 목록 조회
        List<Dibs> dibsList = dibsRepository.findByUser(user);

        // 찜한 상품이 없을 경우 예외 발생
        if (dibsList.isEmpty()) {
            throw new BaseException(CommonErrorCode.NO_DIBS);
        }

        // `Dibs`의 `Product` 데이터를 `ProductDTO`로 매핑하여 반환
        return dibsList.stream()
                .map(dibs -> {
                    var product = dibs.getProduct();
                    return new DibsProductDTO(
                            product.getProductId(),
                            product.getProductImages().stream()
                                    .map(ProductImage::getProductImageUrl) // 이미지 URL 리스트로 변환
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
    }
}
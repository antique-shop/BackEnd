package com.antique.service.user;

import com.antique.domain.Dibs;
import com.antique.domain.Product;
import com.antique.domain.User;
import com.antique.dto.ProductDTO;
import com.antique.exception.dibs.DibsAlreadyExistException;
import com.antique.exception.product.ProductErrorCode;
import com.antique.exception.product.ProductNotFoundException;
import com.antique.exception.user.UserNotFoundException;
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
                .orElseThrow(UserNotFoundException::new);
        // 상품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 중복 체크
        boolean exists = dibsRepository.existsByUserAndProduct(user, product);
        if (exists) {
            throw new DibsAlreadyExistException();
        }

        // 찜 데이터 생성
        Dibs dibs = Dibs.builder()
                .user(user)
                .product(product)
                .build();

        Dibs savedDibs = dibsRepository.save(dibs);
        return savedDibs.getDibsId();
    }


    @Transactional(readOnly = true)
    public List<ProductDTO> getUserDibsProducts(Long userId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 찜 목록 조회
        List<Dibs> dibsList = dibsRepository.findByUser(user);

        // `Dibs`의 `Product` 데이터를 `ProductDTO`로 매핑하여 반환
        return dibsList.stream()
                .map(dibs -> {
                    var product = dibs.getProduct();
                    return new ProductDTO(
                            product.getProductId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getStatus().name(),
                            product.getProductImage(),
                            product.getSeller().getNickname() // 판매자 닉네임 매핑
                    );
                })
                .collect(Collectors.toList());
    }
}
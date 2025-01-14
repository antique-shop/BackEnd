package com.antique.service;

import com.antique.domain.Dibs;
import com.antique.domain.User;
import com.antique.dto.ProductDTO;
import com.antique.exception.user.UserNotFoundException;
import com.antique.repository.DibsRepository;
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
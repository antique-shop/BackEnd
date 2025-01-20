package com.antique.controller.user;

import com.antique.dto.ProductDTO;
import com.antique.service.user.UserDibsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userdibs")
@RequiredArgsConstructor
@Tag(name = "유저 찜 API", description = "사용자의 찜 목록과 관련된 API입니다.")
public class UserDibsController {

    private final UserDibsService userDibsService;

    @Operation(summary = "사용자의 찜 목록 조회", description = "사용자가 찜한 상품 목록을 반환하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true)
    })
    @GetMapping("/{userId}/getdibs")
    public ResponseEntity<List<ProductDTO>> getUserDibsProducts(
            @PathVariable Long userId
    ) {
        // 서비스 호출하여 찜 목록 조회
        List<ProductDTO> products = userDibsService.getUserDibsProducts(userId);

        // 응답 반환
        return ResponseEntity.ok(products);
    }
}

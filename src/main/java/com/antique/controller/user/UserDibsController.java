package com.antique.controller.user;

import com.antique.dto.dibs.DibsProductDTO;
import com.antique.dto.dibs.DibsResponseDTO;
import com.antique.service.jwt.JwtTokenGenerator;
import com.antique.service.user.UserDibsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userdibs")
@RequiredArgsConstructor
@Tag(name = "유저 찜 API", description = "상품 찜하기 관련된 API 목록입니다.")
public class UserDibsController {

    private final UserDibsService userDibsService;
    private final JwtTokenGenerator jwtTokenGenerator; // JWT 파싱을 위한 의존성 추가

    @Operation(summary = "상품 찜 등록", description = "사용자가 특정 상품을 찜하는 API입니다.")
    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT Access Token", required = true),
            @Parameter(name = "productId", description = "상품 ID", required = true)
    })
    @PostMapping("/addDibs")
    public ResponseEntity<DibsResponseDTO> addDibs(
            @RequestHeader("Authorization") String token, // JWT Access Token 받기
            @RequestParam Long productId
    ) {
        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

        // 서비스 호출하여 찜 등록
        Long dibsId = userDibsService.addDibs(userId, productId);

        DibsResponseDTO response = new DibsResponseDTO(
                "찜 생성이 완료되었습니다.",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자의 찜 목록 조회", description = "사용자가 찜한 상품 목록을 반환하는 API입니다.")
    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT Access Token", required = true)
    })
    @GetMapping("/getDibs")
    public ResponseEntity<List<DibsProductDTO>> getUserDibsProducts(
            @RequestHeader("Authorization") String token // JWT Access Token 받기
    ) {
        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

        // 서비스 호출하여 찜 목록 조회
        List<DibsProductDTO> products = userDibsService.getUserDibsProducts(userId);

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품 찜 삭제", description = "사용자가 특정 상품의 찜을 삭제하는 API입니다.")
    @SecurityRequirement(name = "bearerAuth")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT Access Token", required = true),
            @Parameter(name = "productId", description = "상품 ID", required = true)
    })
    @DeleteMapping("/removeDibs")
    public ResponseEntity<DibsResponseDTO> removeDibs(
            @RequestHeader("Authorization") String token, // JWT Access Token 받기
            @RequestParam Long productId
    ) {
        Long userId = jwtTokenGenerator.extractUserId(token); // JWT에서 userId 추출

        // 서비스 호출하여 찜 삭제
        userDibsService.removeDibs(userId, productId);

        DibsResponseDTO response = new DibsResponseDTO(
                "찜 삭제가 완료되었습니다.",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }
}

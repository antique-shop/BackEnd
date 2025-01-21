package com.antique.controller.user;

import com.antique.dto.GenericResponseDTO;
import com.antique.dto.ProductDTO;
import com.antique.dto.dibs.DibsResponseDTO;
import com.antique.service.user.UserDibsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @Operation(summary = "상품 찜 등록", description = "사용자가 특정 상품을 찜하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true),
            @Parameter(name = "productId", description = "상품 ID", required = true)
    })
    @PostMapping("/addDibs")
    public ResponseEntity<DibsResponseDTO> addDibs(
            @RequestParam Long userId,
            @RequestParam Long productId
    ) {
        // 서비스 호출하여 찜 등록
        Long dibsId = userDibsService.addDibs(userId, productId);

        DibsResponseDTO response = new DibsResponseDTO(
                dibsId,
                "찜 생성이 완료되었습니다.",
                HttpStatus.OK.value()
        );
        // 응답 반환
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자의 찜 목록 조회", description = "사용자가 찜한 상품 목록을 반환하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true)
    })
    @GetMapping("/getDibs")
    public ResponseEntity<List<ProductDTO>> getUserDibsProducts(
            @RequestParam Long userId
    ) {
        // 서비스 호출하여 찜 목록 조회
        List<ProductDTO> products = userDibsService.getUserDibsProducts(userId);

        // 응답 반환
        return ResponseEntity.ok(products);
    }
}

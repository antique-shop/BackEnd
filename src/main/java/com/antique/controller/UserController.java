package com.antique.controller;

import com.antique.dto.UserRequestDto;
import com.antique.dto.UserResponseDto;
import com.antique.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "사용자와 관련된 API 목록입니다.")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 정보 입력", description = "사용자의 닉네임과 주소를 설정하는 API입니다.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> updateUserDetails(@RequestBody UserRequestDto userRequestDto) {
        Long userId = userRequestDto.getUserId();
        // 서비스 호출
        Long updatedUserId = userService.updateUserDetails(userId, userRequestDto);

        // 성공 응답 객체 생성
        UserResponseDto responseDto = new UserResponseDto(
                updatedUserId,
                "사용자 정보가 성공적으로 업데이트되었습니다.",
                200 // HTTP 상태 코드
        );

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }
}

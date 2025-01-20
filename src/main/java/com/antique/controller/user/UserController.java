package com.antique.controller.user;

import com.antique.dto.GenericResponseDTO;
import com.antique.dto.user.UpdateAddressDTO;
import com.antique.dto.user.UpdateNicknameDTO;
import com.antique.dto.user.UserRequestDTO;
import com.antique.dto.user.UserResponseDTO;
import com.antique.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "사용자와 관련된 API 목록입니다.")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 정보 입력", description = "사용자의 닉네임과 주소를 설정하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true),
            @Parameter(name = "nickname", description = "사용자 닉네임", required = true),
            @Parameter(name = "address", description = "사용자 주소", required = true)
    })
    @PostMapping("/register")
    public ResponseEntity<GenericResponseDTO> updateUserDetails(@RequestBody UserRequestDTO userRequestDto) {
        Long userId = userRequestDto.getUserId();
        // 서비스 호출
        Long updatedUserId = userService.updateUserDetails(userId, userRequestDto);

        // 성공 응답 객체 생성
        GenericResponseDTO responseDto = new GenericResponseDTO(
                updatedUserId,
                "사용자 정보가 성공적으로 업데이트되었습니다.",
                HttpStatus.OK.value() // HTTP 상태 코드
        );

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 변경하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true),
            @Parameter(name = "nickname", description = "변경할 닉네임", required = true)
    })
    @PostMapping("/updateNickname")
    public ResponseEntity<GenericResponseDTO> updateUserNickname(@RequestBody UpdateNicknameDTO updateNicknameDTO) {
        Long userId = updateNicknameDTO.getUserId();
        String userNickname = updateNicknameDTO.getNickname();
        // 서비스 호출
        Long updatedUserId = userService.updateUserNickname(userId, userNickname);

        // 성공 응답 객체 생성
        GenericResponseDTO responseDto = new GenericResponseDTO(
                updatedUserId,
                "닉네임이 성공적으로 변경되었습니다.",
                HttpStatus.OK.value()// HTTP 상태 코드
        );

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "주소 변경", description = "사용자의 주소를 변경하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true),
            @Parameter(name = "address", description = "변경할 주소", required = true)
    })
    @PostMapping("/updateAddress")
    public ResponseEntity<GenericResponseDTO> updateUserAddress(@RequestBody UpdateAddressDTO updateAddressDTO) {
        Long userId = updateAddressDTO.getUserId();
        String userAddress = updateAddressDTO.getAddress();
        // 서비스 호출
        Long updatedUserId = userService.updateUserAddress(userId, userAddress);

        // 성공 응답 객체 생성
        GenericResponseDTO responseDto = new GenericResponseDTO(
                updatedUserId,
                "사용자 주소가 성공적으로 변경되었습니다.",
                HttpStatus.OK.value() // HTTP 상태 코드
        );

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }
}

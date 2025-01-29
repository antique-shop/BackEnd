package com.antique.controller.user;


import com.antique.dto.user.UpdateNicknameDTO;
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

    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 설정하는 API입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID", required = true),
            @Parameter(name = "nickname", description = "변경할 닉네임", required = true)
    })
    @PostMapping("/updateNickname")
    public ResponseEntity<UserResponseDTO> updateUserNickname(@RequestBody UpdateNicknameDTO updateNicknameDTO) {
        Long userId = updateNicknameDTO.getUserId();
        String userNickname = updateNicknameDTO.getNickname();
        // 서비스 호출
        Long updatedUserId = userService.updateUserNickname(userId, userNickname);

        // 성공 응답 객체 생성
        UserResponseDTO responseDto = new UserResponseDTO(
                updatedUserId,
                "닉네임이 성공적으로 설정되었습니다.",
                HttpStatus.OK.value()// HTTP 상태 코드
        );

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "닉네임 중복 확인", description = "사용자의 닉네임 중복 여부를 확인하는 API입니다.")
    @Parameter(name = "nickname", description = "중복 확인할 닉네임", required = true)
    @GetMapping("/checkNickname")
    public ResponseEntity<UserResponseDTO> checkNickname(@RequestParam String nickname) {
        // 닉네임 중복 여부 확인
        userService.checkNicknameDuplication(nickname);

        // 닉네임이 사용 가능한 경우 200 반환
        UserResponseDTO responseDto = new UserResponseDTO(
                null,
                "해당 닉네임은 사용 가능합니다.",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(responseDto);
    }
}

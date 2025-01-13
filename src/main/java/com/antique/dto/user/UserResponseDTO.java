package com.antique.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드를 JSON에서 제외
public class UserResponseDTO {
    private Long userId;      // 업데이트된 사용자 ID
    private String message;   // 응답 메시지
    private int statusCode;   // HTTP 상태 코드
}
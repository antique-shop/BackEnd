package com.antique.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponseDTO {
    private Long resourceId;  // 리소스 ID (사용자, 상품 등)
    private String message;   // 응답 메시지
    private int statusCode;   // HTTP 상태 코드
}
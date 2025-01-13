package com.antique.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateNicknameDTO {
    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long userId;

    @Schema(description = "변경할 닉네임", example = "UpdatedNickname", required = false)
    private String nickname;
}
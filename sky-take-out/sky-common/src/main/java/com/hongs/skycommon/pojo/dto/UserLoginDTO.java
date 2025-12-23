package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录DTO")
public class UserLoginDTO {

    @Schema(description = "微信授权码")
    private String code;
}

package com.hongs.skycommon.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录VO")
public class UserLoginVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "微信用户openid")
    private String openid;

    @Schema(description = "Jwt令牌")
    private String token;
}

package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "修改密码DTO")
public class EmployeeEditPasswordDTO implements Serializable {

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "旧密码")
    private String oldPassword;

}


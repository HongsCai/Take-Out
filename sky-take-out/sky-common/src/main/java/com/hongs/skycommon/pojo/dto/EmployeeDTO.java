package com.hongs.skycommon.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;


@Data
@Schema(description = "新增员工DTO")
public class EmployeeDTO implements Serializable {

    @Schema(description = "员工ID")
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "身份证号")
    private String idNumber;
}

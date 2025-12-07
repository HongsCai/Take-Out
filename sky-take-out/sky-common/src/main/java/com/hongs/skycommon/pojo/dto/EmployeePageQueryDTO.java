package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "员工分页查询DTO")
public class EmployeePageQueryDTO implements Serializable {

    @Schema(description = "员工姓名")
    private String name;

    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页显示记录数")
    private int pageSize;

}

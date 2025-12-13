package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "套餐分页查询DTO")
public class SetmealPageQueryDTO implements Serializable {
    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页记录数")
    private int pageSize;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "状态")
    private Integer status;
}

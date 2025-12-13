package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "菜品分页查询DTO")
public class DishPageQueryDTO implements Serializable {

    @Schema(description = "菜品名称")
    private String name;

    @Schema(description = "菜品分类id")
    private Long categoryId;

    @Schema(description = "0 停售 1 启售")
    private Integer status;

    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页记录数")
    private int pageSize;
}

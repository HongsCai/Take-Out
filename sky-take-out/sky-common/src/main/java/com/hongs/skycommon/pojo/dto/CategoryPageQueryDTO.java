package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分类分页查询DTO")
public class CategoryPageQueryDTO implements Serializable {

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "页码")
    private Long page;

    @Schema(description = "每页记录数")
    private Long pageSize;

    @Schema(description = "分类类型：1为菜品分类，2为套餐分类")
    private Integer type;
}

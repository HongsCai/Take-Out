package com.hongs.skycommon.pojo.dto;

import com.hongs.skycommon.pojo.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "新增菜品DTO")
public class DishSaveDTO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜品名称")
    private String name;

    @Schema(description = "菜品分类id")
    private Long categoryId;

    @Schema(description = "菜品价格")
    private BigDecimal price;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "0 停售 1 启售")
    private Integer status;

    @Schema(description = "菜品口味")
    private List<DishFlavor> flavors = new ArrayList<>();

}

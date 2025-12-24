package com.hongs.skycommon.pojo.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "根据套餐id查询包含的菜品VO")
public class SetmealWithDishGetByIdVO implements Serializable {

    @Schema(description = "份数")
    Integer copies;

    @Schema(description = "菜品图片路径")
    String description;

    @Schema(description = "图片")
    String image;

    @Schema(description = "菜品名称")
    String name;
}

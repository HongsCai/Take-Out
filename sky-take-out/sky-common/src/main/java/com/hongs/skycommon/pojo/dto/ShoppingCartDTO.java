package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "购物车添加DTO")
public class ShoppingCartDTO implements Serializable {

    @Schema(description = "口味")
    private String dishFlavor;

    @Schema(description = "菜品ID")
    private Long dishId;

    @Schema(description = "套餐ID")
    private Long setmealId;
}

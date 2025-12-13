package com.hongs.skycommon.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "套餐分页查询VO")
@Data
public class SetmealPageQueryVO implements Serializable {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜品分类id")
    private Long categoryId;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐价格")
    private BigDecimal price;

    @Schema(description = "售卖状态 0:停售 1:启售")
    private Integer status;

    @Schema(description = "描述信息")
    private String description;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "分类名称")
    private String categoryName;
}

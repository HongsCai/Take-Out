package com.hongs.skycommon.pojo.vo;

import com.hongs.skycommon.pojo.entity.SetmealDish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "根据id查询套餐VO")
public class SetmealGetOneByIdVO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜品分类id")
    private Long categoryId;

    @Schema(description = "菜品名称")
    private String categoryName;

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

    @Schema(description = "套餐包含的菜品")
    private List<SetmealDish> setmealDishes;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}

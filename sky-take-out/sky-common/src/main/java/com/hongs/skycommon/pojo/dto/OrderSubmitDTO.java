package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户下单DTO")
public class OrderSubmitDTO implements Serializable {

    @Schema(description = "地址id")
    private Long addressBookId;

    @Schema(description = "实收金额")
    private BigDecimal amount;

    @Schema(description = "配送状态  1立即送出  0选择具体时间")
    private Integer deliveryStatus;

    @Schema(description = "预计送达时间")
    private LocalDateTime estimatedDeliveryTime;

    @Schema(description = "打包费")
    private Integer packAmount;

    @Schema(description = "支付方式 1 微信 2 支付宝")
    private Integer payMethod;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "餐具数量")
    private Integer tablewareNumber;

    @Schema(description = "餐具数量状态  1按餐量提供  0选择具体数量")
    private Integer tablewareStatus;

}
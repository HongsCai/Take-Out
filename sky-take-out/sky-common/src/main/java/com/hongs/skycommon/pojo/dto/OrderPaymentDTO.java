package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单支付DTO")
public class OrderPaymentDTO {

    @Schema(description = "订单号")
    private String orderNumber;

    @Schema(description = "支付方式 1 微信 2 支付宝")
    private Integer payMethod;
}




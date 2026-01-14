package com.hongs.skycommon.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户下单VO")
public class OrderSubmitVO implements Serializable {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    @Schema(description = "订单号")
    private String orderNumber;

    @Schema(description = "下单时间")
    private LocalDateTime orderTime;
}
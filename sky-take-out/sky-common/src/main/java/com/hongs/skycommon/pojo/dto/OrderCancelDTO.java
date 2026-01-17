package com.hongs.skycommon.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "历史订单查询DTO")
public class OrderCancelDTO {

    @Schema(description = "订单取消原因")
    private String cancelReason;

    @Schema(description = "订单id")
    private Integer id;
}

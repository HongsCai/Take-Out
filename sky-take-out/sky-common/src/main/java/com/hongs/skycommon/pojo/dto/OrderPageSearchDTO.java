package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "订单搜索DTO")
public class OrderPageSearchDTO {

    @Schema(description = "开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "订单号")
    private String number;

    @Schema(description = "页码")
    private Long page;

    @Schema(description = "每页记录数")
    private Long pageSize;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款")
    private Integer status;

}

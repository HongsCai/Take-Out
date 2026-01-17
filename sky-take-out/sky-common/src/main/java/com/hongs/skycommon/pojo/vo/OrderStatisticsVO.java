package com.hongs.skycommon.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "订单数量统计VO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatisticsVO {

    @Schema(description = "待派送数量")
    private Long confirmed;

    @Schema(description = "派送中数量")
    private Long deliveryInProgress;

    @Schema(description = "待接单数量")
    private Long toBeConfirmed;
}

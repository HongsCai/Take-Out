package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "拒单DTO")
public class OrderRejectionDTO {

    @Schema(description = "订单id")
    Long id;

    @Schema(description = "拒单原因")
    String rejectionReason;
}

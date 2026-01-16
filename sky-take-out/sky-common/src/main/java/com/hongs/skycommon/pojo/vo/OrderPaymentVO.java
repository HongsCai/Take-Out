package com.hongs.skycommon.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单支付VO")
public class OrderPaymentVO {

    @Schema(description = "随机字符串")
    String nonceStr;

    @Schema(description = "预支付交易会话标识")
    String packageStr;

    @Schema(description = "签名")
    String paySign;

    @Schema(description = "签名方式")
    String signType;

    @Schema(description = "时间戳")
    String timeStamp;
}

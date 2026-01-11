package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增地址DTO")
public class AddressBookSaveDTO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "收货人")
    private String consignee;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "省级区划编号")
    private String provinceCode;

    @Schema(description = "省级名称")
    private String provinceName;

    @Schema(description = "市级区划编号")
    private String cityCode;

    @Schema(description = "市级名称")
    private String cityName;

    @Schema(description = "区级区划编号")
    private String districtCode;

    @Schema(description = "区级名称")
    private String districtName;

    @Schema(description = "详细地址")
    private String detail;

    @Schema(description = "标签")
    private String label;

    @Schema(description = "默认 0 否 1是")
    private Integer isDefault;
}

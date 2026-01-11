package com.hongs.skycommon.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "设置默认地址DTO")
public class AddressBookSetDefaultDTO {

    @Schema(description = "地址簿ID")
    private Long id;
}

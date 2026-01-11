package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理
 */
@Slf4j
@Tag(name = "店铺相关接口")
@RestController
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置营业状态
     *
     * @param status
     * @return
     */
    @Operation(summary = "设置营业状态")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置营业状态: {}", status.equals(StatusConstant.ENABLE) ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set("sky_take_out:shop:status", status);
        return Result.success();
    }

    /**
     * 获取营业状态
     *
     * @return
     */
    @Operation(summary = "获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("sky_take_out:shop:status");
        log.info("获得营业状态: {}", status);
        return Result.success(status);
    }
}

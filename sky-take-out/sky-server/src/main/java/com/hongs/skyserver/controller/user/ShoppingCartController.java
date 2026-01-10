package com.hongs.skyserver.controller.user;

import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.pojo.dto.ShoppingCartDTO;
import com.hongs.skycommon.pojo.entity.ShoppingCart;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
@Tag(name = "购物车接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车-商品信息: {}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @Operation(summary = "查询购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listByUserId() {
        log.info("查询购物车-用户ID: {}", BaseContext.getCurrentId());
        return Result.success(shoppingCartService.listByUserId());
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clean")
    public Result clean() {
        log.info("清空购物车-用户ID: {}", BaseContext.getCurrentId());
        shoppingCartService.removeByUserId();
        return Result.success();
    }

    @Operation(summary = "删除购物车中一个商品")
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品-购物车信息: {}", shoppingCartDTO);
        shoppingCartService.remove(shoppingCartDTO);
        return Result.success();
    }
}

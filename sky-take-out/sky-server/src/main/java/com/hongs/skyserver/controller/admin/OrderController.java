package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.OrderCancelDTO;
import com.hongs.skycommon.pojo.dto.OrderConfirmDTO;
import com.hongs.skycommon.pojo.dto.OrderPageSearchDTO;
import com.hongs.skycommon.pojo.dto.OrderRejectionDTO;
import com.hongs.skycommon.pojo.vo.OrderAdminDetailVO;
import com.hongs.skycommon.pojo.vo.OrderPageSearchVO;
import com.hongs.skycommon.pojo.vo.OrderStatisticsVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "订单管理相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "取消订单")
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrderCancelDTO orderCancelDTO) {
        log.info("取消订单: {}", orderCancelDTO);
        orderService.adminCancel(orderCancelDTO);
        return Result.success();
    }

    @Operation(summary = "各个状态的订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        log.info("各个状态的订单数量统计");
        return Result.success(orderService.statistics());
    }

    @Operation(summary = "完成订单")
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单-id: {}", id);
        orderService.complete(id);
        return Result.success();
    }

    @Operation(summary = "拒单")
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrderRejectionDTO orderRejectionDTO) {
        log.info("拒单: {}", orderRejectionDTO);
        orderService.rejection(orderRejectionDTO);
        return Result.success();
    }

    @Operation(summary = "接单")
    @PutMapping("/confirm")
    public Result orderDetail(@RequestBody OrderConfirmDTO orderConfirmDTO) {
        log.info("接单: {}", orderConfirmDTO);
        orderService.confirm(orderConfirmDTO);
        return Result.success();
    }

    @Operation(summary = "查看订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderAdminDetailVO> orderAdminDetail(@PathVariable Long id) {
        log.info("查看订单详情-订单Id: {}", id);
        return Result.success(orderService.orderAdminDetail(id));
    }

    @Operation(summary = "派送订单")
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        log.info("派送订单-订单Id: {}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @Operation(summary = "订单搜索")
    @GetMapping("/conditionSearch")
    public Result<PageResult<OrderPageSearchVO>> conditionSearch(
            @ParameterObject OrderPageSearchDTO orderPageSearchDTO) {
        log.info("订单搜索: {}", orderPageSearchDTO);
        return Result.success(orderService.conditionSearch(orderPageSearchDTO));
    }

}

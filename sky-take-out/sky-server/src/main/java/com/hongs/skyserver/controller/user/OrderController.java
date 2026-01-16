package com.hongs.skyserver.controller.user;


import com.hongs.skycommon.pojo.dto.OrderPaymentDTO;
import com.hongs.skycommon.pojo.dto.OrderSubmitDTO;
import com.hongs.skycommon.pojo.vo.OrderPageQueryVO;
import com.hongs.skycommon.pojo.vo.OrderPaymentVO;
import com.hongs.skycommon.pojo.vo.OrderSubmitVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Tag(name = "用户订单接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO) {
        log.info("用户下单: {}", orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @Operation(summary = "订单支付")
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrderPaymentDTO orderPaymentDTO) {
        log.info("订单支付: {}", orderPaymentDTO);
        return Result.success(orderService.payment(orderPaymentDTO));
    }

    @Operation(summary = "历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult<OrderPageQueryVO>> historyOrders(@RequestBody OrderPageQueryVO orderPageQueryVO) {
        log.info("历史订单查询: {}", orderPageQueryVO);


    }


}

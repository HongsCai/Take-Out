package com.hongs.skyserver.controller.user;


import com.hongs.skycommon.pojo.dto.OrderSubmitDTO;
import com.hongs.skycommon.pojo.vo.OrderSubmitVO;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/order")
@Tag(name = "用户订单接口")
@Slf4j
public class OrderController {

    private OrderService orderService;

    @Operation(summary = "用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO) {
        log.info("用户下单: {}", orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

}

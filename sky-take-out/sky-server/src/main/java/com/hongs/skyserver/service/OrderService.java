package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.OrderSubmitDTO;
import com.hongs.skycommon.pojo.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.OrderSubmitVO;

/**
* @author Hongs
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2026-01-12 23:32:11
*/
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orderSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);



}

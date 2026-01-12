package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.entity.Orders;
import com.hongs.skyserver.service.OrdersService;
import com.hongs.skyserver.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author Hongs
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-01-12 23:32:11
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}





package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.entity.OrderDetail;
import com.hongs.skyserver.service.OrderDetailService;
import com.hongs.skyserver.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Hongs
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2026-01-12 23:32:18
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}





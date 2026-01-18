package com.hongs.skyserver.mapper;

import com.hongs.skycommon.pojo.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongs.skycommon.pojo.vo.OrderStatisticsVO;

/**
* @author Hongs
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2026-01-12 23:32:11
* @Entity com.hongs.skycommon.pojo.entity.Orders
*/
public interface OrderMapper extends BaseMapper<Orders> {

    /**
     * 获取订单统计信息
     * @return
     */
    OrderStatisticsVO getOrderStatistics();
}

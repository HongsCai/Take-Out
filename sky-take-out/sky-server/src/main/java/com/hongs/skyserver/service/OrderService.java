package com.hongs.skyserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.dto.*;
import com.hongs.skycommon.pojo.entity.Orders;
import com.hongs.skycommon.pojo.vo.*;
import com.hongs.skycommon.result.PageResult;

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

    /**
     * 订单支付
     * @param orderPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO);

    /**
     * 支付成功
     * 修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询
     * @param orderPageQueryDTO
     * @return
     */
    PageResult<OrderPageQueryVO> historyOrders(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 订单详情查询
     * @param id
     * @return
     */
    OrderPageQueryVO orderDetail(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 管理端取消订单
     * @param orderCancelDTO
     */
    void adminCancel(OrderCancelDTO orderCancelDTO);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 完成订单
     * @param id
     * @return
     */
    void complete(Long id);

    /**
     * 拒单
     * @param orderRejectionDTO
     */
    void rejection(OrderRejectionDTO orderRejectionDTO);

    /**
     * 接单
     * @param orderConfirmDTO
     */
    void confirm(OrderConfirmDTO orderConfirmDTO);

    /**
     * 管理端查看订单详情
     * @param id
     * @return
     */
    OrderAdminDetailVO orderAdminDetail(Long id);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 订单搜索
     * @param orderPageSearchDTO
     * @return
     */
    PageResult<OrderPageSearchVO> conditionSearch(OrderPageSearchDTO orderPageSearchDTO);

    /**
     * 处理支付超时订单
     */
    void processPayTimeOutOrder();

    /**
     * 处理派送超时订单
     */
    void processDeliveryTimeOutOrder();

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);
}

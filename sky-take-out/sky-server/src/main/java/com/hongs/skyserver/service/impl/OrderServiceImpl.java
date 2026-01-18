package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.OrderConstant;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.exception.AddressBookBusinessException;
import com.hongs.skycommon.exception.OrderBusinessException;
import com.hongs.skycommon.exception.ShoppingCartBusinessException;
import com.hongs.skycommon.pojo.dto.*;
import com.hongs.skycommon.pojo.entity.AddressBook;
import com.hongs.skycommon.pojo.entity.OrderDetail;
import com.hongs.skycommon.pojo.entity.Orders;
import com.hongs.skycommon.pojo.entity.ShoppingCart;
import com.hongs.skycommon.pojo.vo.*;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skyserver.mapper.OrderMapper;
import com.hongs.skyserver.service.AddressBookService;
import com.hongs.skyserver.service.OrderDetailService;
import com.hongs.skyserver.service.OrderService;
import com.hongs.skyserver.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author Hongs
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-01-12 23:32:11
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders>
    implements OrderService {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orderSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {

        AddressBook addressBook = addressBookService.getOne(
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                        .eq(AddressBook::getId, orderSubmitDTO.getAddressBookId()));

        // 处理地址为空的情况
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUserId();
        // 处理购物车当前为空的情况
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 订单金额校验
        BigDecimal amount = shoppingCartList.stream()
                .map(item -> item.getAmount().multiply(BigDecimal.valueOf(item.getNumber())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (orderSubmitDTO.getAmount().compareTo(amount) < 0) {
            throw new OrderBusinessException(MessageConstant.UNKNOWN_ERROR);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, orders);
        orders.setNumber(System.currentTimeMillis() + String.valueOf((int)((Math.random() * 9 + 1) * 1000)));
        orders.setStatus(OrderConstant.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setUserId(addressBook.getUserId());
        orders.setPayStatus(OrderConstant.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName()
                + addressBook.getDistrictName() + addressBook.getDetail());

        // TODO 这里username没有设置 后期加上
        // orders.setUserName();
        orders.setConsignee(addressBook.getConsignee());

        // 订单表的添加
        this.save(orders);

        // 订单详细表的批量添加
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetailList);

        // 清理购物车
        shoppingCartService.removeByUserId();

        // 生成返回的VO
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     * @param orderPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO) {
        // TODO 由于无商户证明，这里跳过微信支付

        paySuccess(orderPaymentDTO.getOrderNumber());

        return new OrderPaymentVO();
    }

    /**
     * 支付成功
     * 修改订单状态
     * @param outTradeNo
     */
    @Override
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询订单
        // 根据订单号查询订单并更新更新订单的状态、支付方式、支付状态、结账时间
        this.update(new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getNumber, outTradeNo)
                .set(Orders::getStatus, OrderConstant.TO_BE_CONFIRMED)
                .set(Orders::getPayMethod, OrderConstant.WECHAT)
                .set(Orders::getPayStatus, OrderConstant.PAID)
                .set(Orders::getCheckoutTime, LocalDateTime.now()));
    }

    /**
     * 历史订单查询
     * @param orderPageQueryDTO
     * @return
     */
    @Override
    public PageResult<OrderPageQueryVO> historyOrders(OrderPageQueryDTO orderPageQueryDTO) {
        Page<Orders> page = new Page(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());
        this.page(page, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, BaseContext.getCurrentId())
                .eq(orderPageQueryDTO.getStatus() != null, Orders::getStatus, orderPageQueryDTO.getStatus())
                .orderByDesc(Orders::getOrderTime));

        List<Long> OrderIdList = page.getRecords().stream()
                .map(Orders::getId).collect(Collectors.toList());

        if (OrderIdList.isEmpty()) {
            return new PageResult<>(page.getTotal(), new ArrayList<>());
        }

        List<OrderDetail> orderDetailList = orderDetailService.list(
                new LambdaQueryWrapper<OrderDetail>().in(OrderDetail::getOrderId, OrderIdList));

        Map<Long, List<OrderDetail>> orderDetailMap = orderDetailList.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));

        List<OrderPageQueryVO> orderPageQueryVOList = page.getRecords().stream()
                .map(item -> {
                    OrderPageQueryVO orderPageQueryVO = new OrderPageQueryVO();
                    BeanUtils.copyProperties(item, orderPageQueryVO);
                    orderPageQueryVO.setOrderDetailList(orderDetailMap.getOrDefault(item.getId(), new ArrayList<>()));
                    return orderPageQueryVO;
                }).collect(Collectors.toList());

        return new PageResult<>(page.getTotal(), orderPageQueryVOList);
    }

    /**
     * 订单详情查询
     * @param id
     * @return
     */
    @Override
    public OrderPageQueryVO orderDetail(Long id) {

        Orders orders = this.getOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getId, id)
                .eq(Orders::getUserId, BaseContext.getCurrentId()));

        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        OrderPageQueryVO orderPageQueryVO = new OrderPageQueryVO();
        BeanUtils.copyProperties(orders, orderPageQueryVO);

        orderPageQueryVO.setOrderDetailList(orderDetailService.list(
                new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, id)));

        return orderPageQueryVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    @Transactional
    public void cancel(Long id) {
        Orders ordersDB = this.getOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getId, id)
                .eq(Orders::getUserId, BaseContext.getCurrentId()));

        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 构造更新条件
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, id)
                .set(Orders::getStatus, OrderConstant.CANCELLED)
                .set(Orders::getCancelReason, "用户取消")
                .set(Orders::getCancelTime, LocalDateTime.now());

        // 退款逻辑处理
        if (ordersDB.getStatus().equals(OrderConstant.TO_BE_CONFIRMED)) {

            // TODO 跳过微信支付

//            // 调用微信支付退款接口
//            weChatPayUtil.refund(
//                    ordersDB.getNumber(), //商户订单号
//                    ordersDB.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            updateWrapper.set(Orders::getPayStatus, OrderConstant.REFUND);
        }

        this.update(updateWrapper);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    @Transactional
    public void repetition(Long id) {

        Orders orders = this.getOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getId, id)
                .eq(Orders::getUserId, BaseContext.getCurrentId()));

        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        List<OrderDetail> orderDetailList = orderDetailService.list(
                new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, id));

        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(
                item -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    BeanUtils.copyProperties(item, shoppingCart);
                    shoppingCart.setId(null);
                    shoppingCart.setUserId(BaseContext.getCurrentId());
                    shoppingCart.setCreateTime(LocalDateTime.now());
                    return shoppingCart;
                }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCartList);
    }

    /**
     * 管理端取消订单
     * @param orderCancelDTO
     */
    @Override
    @Transactional
    public void adminCancel(OrderCancelDTO orderCancelDTO) {

        Orders ordersDB = this.getById(orderCancelDTO.getId());

        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 构造更新条件
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, orderCancelDTO.getId())
                .set(Orders::getStatus, OrderConstant.CANCELLED)
                .set(Orders::getCancelReason, orderCancelDTO.getCancelReason())
                .set(Orders::getCancelTime, LocalDateTime.now());

        // 退款逻辑处理
        if (ordersDB.getPayStatus().equals(OrderConstant.PAID)) {
            // TODO 跳过微信支付

//            // 调用微信支付退款接口
//            weChatPayUtil.refund(
//                    ordersDB.getNumber(), //商户订单号
//                    ordersDB.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            updateWrapper.set(Orders::getPayStatus, OrderConstant.REFUND);
        }
        this.update(updateWrapper);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        return this.baseMapper.getOrderStatistics();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @Override
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = this.getById(id);

        // 校验订单是否存在，并且状态为派送中
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        } else if (!ordersDB.getStatus().equals(OrderConstant.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 更新订单状态，状态转为完成
        this.update(new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, id)
                .set(Orders::getStatus, OrderConstant.COMPLETED)
                .set(Orders::getDeliveryTime, LocalDateTime.now()));
    }

    /**
     * 拒单
     * @param orderRejectionDTO
     */
    @Override
    public void rejection(OrderRejectionDTO orderRejectionDTO) {
        Orders ordersDB = this.getById(orderRejectionDTO.getId());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        } else if (!ordersDB.getStatus().equals(OrderConstant.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // TODO 跳过微信支付
//        // 调用微信支付退款接口
//        weChatPayUtil.refund(
//                ordersDB.getNumber(), //商户订单号
//                ordersDB.getNumber(), //商户退款单号
//                new BigDecimal(0.01),//退款金额，单位 元
//                new BigDecimal(0.01));//原订单金额

        this.update(new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, orderRejectionDTO.getId())
                .set(Orders::getStatus, OrderConstant.CANCELLED)
                .set(Orders::getRejectionReason, orderRejectionDTO.getRejectionReason())
                .set(Orders::getCancelTime, LocalDateTime.now())
                .set(Orders::getPayStatus, OrderConstant.REFUND));
    }

    /**
     * 接单
     * @param orderConfirmDTO
     */
    @Override
    public void confirm(OrderConfirmDTO orderConfirmDTO) {
        Orders ordersDB = this.getById(orderConfirmDTO.getId());
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        } else if (!ordersDB.getStatus().equals(OrderConstant.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        this.update(new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, orderConfirmDTO.getId())
                .set(Orders::getStatus, OrderConstant.CONFIRMED));
    }

    /**
     * 管理端查看订单详情
     * @param id
     * @return
     */
    @Override
    public OrderAdminDetailVO orderAdminDetail(Long id) {
        Orders orders = this.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        OrderAdminDetailVO orderAdminDetailVO = new OrderAdminDetailVO();
        BeanUtils.copyProperties(orders, orderAdminDetailVO);

        // 获取订单菜品-数组
        orderAdminDetailVO.setOrderDetailList(orderDetailService.list(
                new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, id)));

        // 获取订单菜品-字符串形式
        orderAdminDetailVO.setOrderDishes(getDishesStr(orderAdminDetailVO.getOrderDetailList()));

        return orderAdminDetailVO;
    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void delivery(Long id) {
        Orders ordersDB = this.getById(id);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        } else if (!ordersDB.getStatus().equals(OrderConstant.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        this.update(new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, id)
                .set(Orders::getStatus, OrderConstant.DELIVERY_IN_PROGRESS));
    }

    /**
     * 订单搜索
     * @param orderPageSearchDTO
     * @return
     */
    @Override
    public PageResult<OrderPageSearchVO> conditionSearch(OrderPageSearchDTO orderPageSearchDTO) {
        // 分页查询订单主表
        Page<Orders> page = new Page<>(orderPageSearchDTO.getPage(), orderPageSearchDTO.getPageSize());
        this.page(page, new LambdaQueryWrapper<Orders>()
                .like(orderPageSearchDTO.getNumber() != null, Orders::getNumber, orderPageSearchDTO.getNumber())
                .like(orderPageSearchDTO.getPhone() != null, Orders::getPhone, orderPageSearchDTO.getPhone())
                .eq(orderPageSearchDTO.getStatus() != null, Orders::getStatus, orderPageSearchDTO.getStatus())
                .ge(orderPageSearchDTO.getBeginTime() != null, Orders::getOrderTime, orderPageSearchDTO.getBeginTime())
                .le(orderPageSearchDTO.getEndTime() != null, Orders::getOrderTime, orderPageSearchDTO.getEndTime())
                .orderByDesc(Orders::getOrderTime));

        List<Orders> records = page.getRecords();
        if (records.isEmpty()) {
            return new PageResult<>(page.getTotal(), new ArrayList<>());
        }

        // 批量获取详情并按照 orderId 分组
        List<Long> orderIds = records.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderDetail> details = orderDetailService.list(
                new LambdaQueryWrapper<OrderDetail>().in(OrderDetail::getOrderId, orderIds));

        Map<Long, List<OrderDetail>> detailGroup = details.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));

        // 组装 VO 列表
        List<OrderPageSearchVO> voList = records.stream().map(item -> {
            OrderPageSearchVO vo = new OrderPageSearchVO();
            BeanUtils.copyProperties(item, vo);

            // 提取菜品明细字符串
            List<OrderDetail> orderDetails = detailGroup.getOrDefault(item.getId(), new ArrayList<>());
            vo.setOrderDishes(getDishesStr(orderDetails));

            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(page.getTotal(), voList);
    }

    /**
     * 辅助方法：将订单详情转化为 "菜品(口味)*数量; " 格式
     * @param details
     * @return
     */
    private String getDishesStr(List<OrderDetail> details) {
        return details.stream().map(detail -> {
            String flavor = (detail.getDishFlavor() == null || detail.getDishFlavor().isEmpty())
                    ? "" : "(" + detail.getDishFlavor() + ")";
            return detail.getName() + flavor + "*" + detail.getNumber();
        }).collect(Collectors.joining("; ")); // 使用分号+空格分隔，且结尾不会有多余的分号
    }
}

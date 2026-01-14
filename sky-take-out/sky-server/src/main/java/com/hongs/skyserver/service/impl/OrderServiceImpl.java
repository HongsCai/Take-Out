package com.hongs.skyserver.service.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.OrderConstant;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.exception.AddressBookBusinessException;
import com.hongs.skycommon.exception.OrderBusinessException;
import com.hongs.skycommon.exception.ShoppingCartBusinessException;
import com.hongs.skycommon.pojo.dto.OrderSubmitDTO;
import com.hongs.skycommon.pojo.entity.AddressBook;
import com.hongs.skycommon.pojo.entity.OrderDetail;
import com.hongs.skycommon.pojo.entity.Orders;
import com.hongs.skycommon.pojo.entity.ShoppingCart;
import com.hongs.skycommon.pojo.vo.OrderSubmitVO;
import com.hongs.skyserver.mapper.OrderMapper;
import com.hongs.skyserver.service.AddressBookService;
import com.hongs.skyserver.service.OrderDetailService;
import com.hongs.skyserver.service.OrderService;
import com.hongs.skyserver.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        AddressBook addressBook = addressBookService.getById(orderSubmitDTO.getAddressBookId());
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

        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, orders);

        orders.setAmount(amount);
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
}

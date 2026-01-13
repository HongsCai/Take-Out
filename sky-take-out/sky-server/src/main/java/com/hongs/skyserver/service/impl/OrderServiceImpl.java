package com.hongs.skyserver.service.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.exception.AddressBookBusinessException;
import com.hongs.skycommon.exception.ShoppingCartBusinessException;
import com.hongs.skycommon.pojo.dto.OrderSubmitDTO;
import com.hongs.skycommon.pojo.entity.AddressBook;
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
        if (shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, orders);

        orders.setNumber("");
        orders.setStatus(0);
        orders.setUserId(addressBook.getUserId());
        orders.setPayStatus(0);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.get);
        orders.setUserName("");
        orders.setConsignee("");



    }
}





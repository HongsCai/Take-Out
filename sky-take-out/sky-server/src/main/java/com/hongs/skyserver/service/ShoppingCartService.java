package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.ShoppingCartDTO;
import com.hongs.skycommon.pojo.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Hongs
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2026-01-09 22:22:52
*/
public interface ShoppingCartService extends IService<ShoppingCart> {


    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);


    /**
     * 查询购物车
     * @return
     */
    List<ShoppingCart> listByUserId();


    /**
     * 清空购物车
     */
    void removeByUserId();

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void remove(ShoppingCartDTO shoppingCartDTO);


}

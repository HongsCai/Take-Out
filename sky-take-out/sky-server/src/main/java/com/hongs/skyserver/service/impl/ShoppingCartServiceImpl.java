package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.pojo.dto.ShoppingCartDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.entity.Setmeal;
import com.hongs.skycommon.pojo.entity.ShoppingCart;
import com.hongs.skyserver.mapper.ShoppingCartMapper;
import com.hongs.skyserver.service.DishService;
import com.hongs.skyserver.service.SetmealService;
import com.hongs.skyserver.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Hongs
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2026-01-09 22:22:52
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = this.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .eq(shoppingCartDTO.getDishId() != null,
                        ShoppingCart::getDishId, shoppingCartDTO.getDishId())
                .eq(shoppingCartDTO.getDishFlavor() != null,
                        ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor())
                .eq(shoppingCartDTO.getSetmealId() != null,
                        ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId()));
        if (shoppingCart != null) {
            shoppingCart.setAmount(shoppingCart.getAmount().add(shoppingCart.getAmount().divide(BigDecimal.valueOf(shoppingCart.getNumber()))));
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            this.updateById(shoppingCart);
        } else {
            shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());

            if (shoppingCart.getDishId() != null) {
                Dish dish = dishService.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setNumber(1);
            } else {
                Setmeal setmeal = setmealService.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setNumber(1);
            }
            this.save(shoppingCart);
        }
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart> listByUserId() {
        return this.list(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .orderByAsc(ShoppingCart::getCreateTime));
    }

    /**
     * 清空购物车
     */
    @Override
    public void removeByUserId() {
        this.remove(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void remove(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = this.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .eq(shoppingCartDTO.getDishId() != null,
                        ShoppingCart::getDishId, shoppingCartDTO.getDishId())
                .eq(shoppingCartDTO.getDishFlavor() != null,
                        ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor())
                .eq(shoppingCartDTO.getSetmealId() != null,
                        ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId()));

        if (shoppingCart.getNumber() == 1) {
            this.remove(new LambdaQueryWrapper<ShoppingCart>()
                    .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                    .eq(shoppingCartDTO.getDishId() != null,
                            ShoppingCart::getDishId, shoppingCartDTO.getDishId())
                    .eq(shoppingCartDTO.getDishFlavor() != null,
                            ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor())
                    .eq(shoppingCartDTO.getSetmealId() != null,
                            ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId()));
        } else {
            shoppingCart.setAmount(shoppingCart.getAmount().subtract(shoppingCart.getAmount().divide(BigDecimal.valueOf(shoppingCart.getNumber()))));
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
            this.updateById(shoppingCart);
        }
    }
}

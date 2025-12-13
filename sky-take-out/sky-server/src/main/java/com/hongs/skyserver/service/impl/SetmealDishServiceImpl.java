package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.hongs.skyserver.service.SetmealDishService;
import com.hongs.skyserver.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Hongs
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service实现
* @createDate 2025-12-11 19:59:31
*/
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
    implements SetmealDishService{

    /**
     * 根据菜品id查询套餐id
     * @param dishId
     * @return
     */
    @Override
    public List<Long> getSetmealIdsByDishId(Long dishId) {
        List<SetmealDish> setmealDishes = this.list(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getDishId, dishId));
        return setmealDishes.stream().map(setmealDish -> {
            return setmealDish.getSetmealId();
        }).toList();
    }
}





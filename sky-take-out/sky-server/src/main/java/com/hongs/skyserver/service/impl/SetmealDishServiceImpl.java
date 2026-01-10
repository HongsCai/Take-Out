package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.hongs.skycommon.pojo.vo.SetmealWithDishGetByIdVO;
import com.hongs.skyserver.service.SetmealDishService;
import com.hongs.skyserver.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        }).collect(Collectors.toList());
    }

    /**
     * 根据套餐id查询菜品id
     * @param setmealId
     * @return
     */
    @Override
    public List<Long> getDishIdsBySetmealId(Long setmealId) {
        List<SetmealDish> setmealDishList = this.list(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, setmealId));
        return setmealDishList.stream()
                .map(SetmealDish::getDishId)
                .collect(Collectors.toList());
    }

    @Override
    public List<SetmealWithDishGetByIdVO> getWithDishById(Long id) {
        return this.baseMapper.getWithDishById(id);
    }
}





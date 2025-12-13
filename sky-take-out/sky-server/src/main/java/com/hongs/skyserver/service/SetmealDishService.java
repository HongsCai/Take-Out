package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Hongs
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service
* @createDate 2025-12-11 19:59:31
*/
public interface SetmealDishService extends IService<SetmealDish> {


    /**
     * 根据菜品id查询套餐id
     * @param dishId
     * @return
     */
    List<Long> getSetmealIdsByDishId(Long dishId);
}

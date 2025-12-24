package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.SetmealWithDishGetByIdVO;

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

    /**
     * 根据套餐id查询菜品id
     * @param setmealId
     * @return
     */
    List<Long> getDishIdsBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<SetmealWithDishGetByIdVO> getWithDishById(Long id);
}

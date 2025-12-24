package com.hongs.skyserver.mapper;

import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongs.skycommon.pojo.vo.SetmealWithDishGetByIdVO;

import java.util.List;

/**
* @author Hongs
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
* @createDate 2025-12-11 19:59:31
* @Entity com.hongs.skycommon.pojo.entity.SetmealDish
*/
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<SetmealWithDishGetByIdVO> getWithDishById(Long id);
}





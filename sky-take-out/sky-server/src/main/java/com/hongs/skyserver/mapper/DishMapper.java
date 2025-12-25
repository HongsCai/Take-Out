package com.hongs.skyserver.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongs.skycommon.pojo.dto.DishPageQueryDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongs.skycommon.pojo.vo.DishGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.DishPageQueryVO;

import java.util.List;


/**
* @author Hongs
* @description 针对表【dish(菜品)】的数据库操作Mapper
* @createDate 2025-12-08 18:24:50
* @Entity com/hongs/skyserver.pojo.Dish
*/
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 菜品分页查询
     * @param page
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishPageQueryVO> pageQuery(Page<DishPageQueryVO> page, DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishGetOneByIdVO getOneById(Long id);

}





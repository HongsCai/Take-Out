package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.pojo.dto.DishSaveDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.entity.DishFlavor;
import com.hongs.skyserver.mapper.DishMapper;
import com.hongs.skyserver.service.DishFlavorService;
import com.hongs.skyserver.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Hongs
* @description 针对表【dish(菜品)】的数据库操作Service实现
* @createDate 2025-12-08 18:24:50
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 保存菜品
     * @param dishSaveDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishSaveDTO dishSaveDTO) {
        // 向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishSaveDTO, dish);
        this.save(dish);

        // 向口味表插入多条数据
        List<DishFlavor> dishFlavors = dishSaveDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorService.saveBatch(dishFlavors);
        }
    }
}





package com.hongs.skyserver.service.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.exception.BaseException;
import com.hongs.skycommon.pojo.dto.DishPageQueryDTO;
import com.hongs.skycommon.pojo.dto.DishSaveDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.entity.DishFlavor;
import com.hongs.skycommon.pojo.entity.Setmeal;
import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.hongs.skycommon.pojo.vo.DishGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.DishPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skyserver.mapper.DishMapper;
import com.hongs.skyserver.service.DishFlavorService;
import com.hongs.skyserver.service.DishService;
import com.hongs.skyserver.service.SetmealDishService;
import com.hongs.skyserver.service.SetmealService;
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

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult<DishPageQueryVO> page(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishPageQueryVO> page = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        this.baseMapper.pageQuery(page, dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatchByIds(List<Long> ids) {
        // 判断当前菜品是否在售中
        if (this.count(new LambdaQueryWrapper<Dish>().in(Dish::getId, ids).eq(Dish::getStatus, StatusConstant.ENABLE)) > 0) {
            throw new BaseException(MessageConstant.DISH_ON_SALE);
        }

        // 判断当前菜品是否被套餐关联
        if (setmealDishService.count(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getDishId, ids)) > 0) {
            throw new BaseException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品口味表中的数据
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().in(DishFlavor::getDishId, ids));
        // 删除菜品表中的数据
        this.removeByIds(ids);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    @Transactional
    public DishGetOneByIdVO getOneById(Long id) {
        DishGetOneByIdVO dishGetOneByIdVO = new DishGetOneByIdVO();
        BeanUtils.copyProperties(this.getById(id), dishGetOneByIdVO);
        dishGetOneByIdVO.setFlavors(dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id)));
        return dishGetOneByIdVO;
    }

    /**
     * 根据分类的id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, StatusConstant.ENABLE)
                .orderByDesc(Dish::getUpdateTime);
        return this.list(queryWrapper);
    }

    /**
     * 修改菜品
     * @param dishSaveDTO
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishSaveDTO dishSaveDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishSaveDTO, dish);
        this.updateById(dish);
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishSaveDTO.getId()));

        // 向口味表插入多条数据
        List<DishFlavor> dishFlavors = dishSaveDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {
            dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorService.saveBatch(dishFlavors);
        }
    }

    /**
     * 菜品启售停售
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 如果是停售操作，需要处理关联的套餐
        if (status.equals(StatusConstant.DISABLE)) {
            // 查询包含当前菜品的关联数据
            List<Long> setmealIds = setmealDishService.getSetmealIdsByDishId(id); // 需要在Service里补充这个方法

            if (setmealIds != null && !setmealIds.isEmpty()) {
                // 将这些关联的套餐状态也改为停售
                Setmeal setmeal = Setmeal.builder()
                        .status(StatusConstant.DISABLE)
                        .build();
                LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(Setmeal::getId, setmealIds);
                setmealService.update(setmeal, wrapper);
            }
        }

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        this.updateById(dish);
    }
}





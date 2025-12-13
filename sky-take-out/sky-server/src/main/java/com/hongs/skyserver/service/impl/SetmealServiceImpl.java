package com.hongs.skyserver.service.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.exception.DeletionNotAllowedException;
import com.hongs.skycommon.exception.SetMealEnableFailedException;
import com.hongs.skycommon.pojo.dto.SetmealPageQueryDTO;
import com.hongs.skycommon.pojo.dto.SetmealSaveDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.entity.Setmeal;
import com.hongs.skycommon.pojo.entity.SetmealDish;
import com.hongs.skycommon.pojo.vo.SetmealPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skyserver.service.DishService;
import com.hongs.skyserver.service.SetmealDishService;
import com.hongs.skyserver.service.SetmealService;
import com.hongs.skyserver.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Hongs
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2025-12-08 18:28:18
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     * @param setmealSaveDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealSaveDTO setmealSaveDTO) {
        // 保存套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealSaveDTO, setmeal);
        this.save(setmeal);

        // 保存套餐和菜品的关联信息
        List<SetmealDish> setmealDishList = setmealSaveDTO.getSetmealDishes();
        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
            setmealDishService.saveBatch(setmealDishList);
        }
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult<SetmealPageQueryVO> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<SetmealPageQueryVO> page = new Page<>(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        this.baseMapper.pageQuery(page, setmealPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatchByIds(List<Long> ids) {
        if (this.count(new LambdaQueryWrapper<Setmeal>()
                .in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, StatusConstant.ENABLE)) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>()
                .in(SetmealDish::getSetmealId, ids));
        this.removeByIds(ids);
    }

    /**
     * 修改套餐
     * @param setmealSaveDTO
     * @return
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealSaveDTO setmealSaveDTO) {
        // 修改套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealSaveDTO, setmeal);
        this.updateById(setmeal);

        // 删除套餐中的菜品数据
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, setmeal.getId()));

        // 向套餐菜品表插入多条数据
        List<SetmealDish> setmealDishList = setmealSaveDTO.getSetmealDishes();

        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            setmealDishList.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
            setmealDishService.saveBatch(setmealDishList);
        }
    }

    /**
     * 套餐启售停售
     * @param status
     * @param id
     * @return
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 判断当前是否要更改为启售
        if (status.equals(StatusConstant.ENABLE)) {
            // 查询当前套餐的菜品数据
            List<Long> dishIdList = setmealDishService.getDishIdsBySetmealId(id);
            if (dishIdList != null && !dishIdList.isEmpty()) {
                Long count = dishService.count(new LambdaQueryWrapper<Dish>()
                        .in(Dish::getId, dishIdList)
                        .eq(Dish::getStatus, StatusConstant.DISABLE));
                // 如果存在停售的菜品，则抛出异常
                if (count > 0) {
                    throw new SetMealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        this.updateById(setmeal);
    }
}

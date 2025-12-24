package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.SetmealPageQueryDTO;
import com.hongs.skycommon.pojo.dto.SetmealSaveDTO;
import com.hongs.skycommon.pojo.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.SetmealGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.SetmealPageQueryVO;
import com.hongs.skycommon.pojo.vo.SetmealWithDishGetByIdVO;
import com.hongs.skycommon.result.PageResult;

import java.util.List;

/**
* @author Hongs
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2025-12-08 18:28:18
*/
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐
     * @param setmealSaveDTO
     */
    void saveWithDish(SetmealSaveDTO setmealSaveDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult<SetmealPageQueryVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealGetOneByIdVO getOneById(Long id);

    /**
     * 修改套餐
     * @param setmealSaveDTO
     */
    void updateWithDish(SetmealSaveDTO setmealSaveDTO);

    /**
     * 套餐启售停售
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);


    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    List<Setmeal> listByCategoryId(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<SetmealWithDishGetByIdVO> getWithDishById(Long id);
}

package com.hongs.skyserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.dto.CategoryPageQueryDTO;
import com.hongs.skycommon.pojo.dto.CategorySaveDTO;
import com.hongs.skycommon.pojo.dto.CategoryUpdateInfoDTO;
import com.hongs.skycommon.pojo.entity.Category;
import com.hongs.skycommon.pojo.vo.CategoryPageQueryVO;
import com.hongs.skycommon.result.PageResult;

import java.util.List;

/**
* @author Hongs
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2025-12-08 12:12:19
*/
public interface CategoryService extends IService<Category> {

    /**
     * 新增分类
     * @param categorySaveDTO
     */
    void save(CategorySaveDTO categorySaveDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult<CategoryPageQueryVO> page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    void updateStatus(Integer status, Long id);

    /**
     * 修改分类
     * @param categorySaveDTO
     * @return
     */
    void updateInfo(CategoryUpdateInfoDTO categorySaveDTO);

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> listByType(Integer type);
}

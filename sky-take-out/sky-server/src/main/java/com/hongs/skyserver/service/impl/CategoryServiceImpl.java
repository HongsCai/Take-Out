package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.exception.DeletionNotAllowedException;
import com.hongs.skycommon.pojo.dto.CategoryPageQueryDTO;
import com.hongs.skycommon.pojo.dto.CategorySaveDTO;
import com.hongs.skycommon.pojo.dto.CategoryUpdateInfoDTO;
import com.hongs.skycommon.pojo.entity.Category;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.entity.Setmeal;
import com.hongs.skycommon.pojo.vo.CategoryPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skyserver.mapper.CategoryMapper;
import com.hongs.skyserver.mapper.DishMapper;
import com.hongs.skyserver.mapper.SetmealMapper;
import com.hongs.skyserver.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Hongs
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2025-12-08 12:12:19
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categorySaveDTO
     */
    @Override
    public void save(CategorySaveDTO categorySaveDTO) {
        Category category = Category.builder()
                .id(categorySaveDTO.getId())
                .name(categorySaveDTO.getName())
                .sort(categorySaveDTO.getSort())
                .type(categorySaveDTO.getType())
                .status(StatusConstant.DISABLE)
                .build();
        this.save(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult<CategoryPageQueryVO> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        IPage<Category> iPage = new Page<>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(categoryPageQueryDTO.getName()), Category::getName, categoryPageQueryDTO.getName())
                .eq(categoryPageQueryDTO.getType() != null, Category::getType, categoryPageQueryDTO.getType())
                .orderByAsc(Category::getSort);
        this.page(iPage, wrapper);
        List<CategoryPageQueryVO> records = iPage.getRecords().stream().map(category -> {
            CategoryPageQueryVO vo = new CategoryPageQueryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).toList();
        return new PageResult<>(iPage.getTotal(), records);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        this.updateById(category);
    }

    /**
     * 修改分类
     * @param categoryUpdateInfoDTO
     * @return
     */
    @Override
    public void updateInfo(CategoryUpdateInfoDTO categoryUpdateInfoDTO) {
        Category category = Category.builder()
                .id(categoryUpdateInfoDTO.getId())
                .name(categoryUpdateInfoDTO.getName())
                .sort(categoryUpdateInfoDTO.getSort())
                .build();
        this.updateById(category);
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    @Override
    public void deleteById(Long id) {
        Long count = dishMapper.selectCount(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, id));
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count = setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, id));
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        this.removeById(id);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> listByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null, Category::getType, type);
        return this.list(wrapper);
    }
}





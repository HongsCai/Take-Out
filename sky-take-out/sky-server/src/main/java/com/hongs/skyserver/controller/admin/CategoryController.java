package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.CategoryPageQueryDTO;
import com.hongs.skycommon.pojo.dto.CategorySaveDTO;
import com.hongs.skycommon.pojo.dto.CategoryUpdateInfoDTO;
import com.hongs.skycommon.pojo.entity.Category;
import com.hongs.skycommon.pojo.vo.CategoryPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Tag(name = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categorySaveDTO
     */
    @Operation(summary = "新增分类")
    @PostMapping
    public Result save(@RequestBody CategorySaveDTO categorySaveDTO) {
        log.info("新增分类: {}", categorySaveDTO);
        categoryService.save(categorySaveDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Operation(summary = "分类分页查询")
    @GetMapping("/page")
    public Result<PageResult<CategoryPageQueryVO>> page(@ParameterObject CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询: {}", categoryPageQueryDTO);
        PageResult<CategoryPageQueryVO> pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "启用禁用分类")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用分类: {}, {}", status, id);
        categoryService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryUpdateInfoDTO
     * @return
     */
    @Operation(summary = "修改分类信息")
    @PutMapping
    public Result updateInfo(@RequestBody CategoryUpdateInfoDTO categoryUpdateInfoDTO) {
        log.info("修改分类信息: {}", categoryUpdateInfoDTO);
        categoryService.updateInfo(categoryUpdateInfoDTO);
        return Result.success();
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    @Operation(summary = "根据ID删除分类")
    @DeleteMapping
    public Result deleteById(Long id) {
        log.info("根据ID删除分类: {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Operation(summary = "根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> listByType(Integer type) {
        log.info("根据类型查询分类: {}", type);
        List<Category> categoryList = categoryService.listByType(type);
        return Result.success(categoryList);
    }
}

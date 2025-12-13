package com.hongs.skyserver.controller.admin;


import com.hongs.skycommon.pojo.dto.DishPageQueryDTO;
import com.hongs.skycommon.pojo.dto.DishSaveDTO;
import com.hongs.skycommon.pojo.entity.Dish;
import com.hongs.skycommon.pojo.vo.DishGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.DishPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishSaveDTO
     * @return
     */
    @Operation(summary = "新增菜品")
    @PostMapping
    public Result save(@RequestBody DishSaveDTO dishSaveDTO) {
        log.info("新增菜品: {}", dishSaveDTO);
        dishService.saveWithFlavor(dishSaveDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Operation(summary = "菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(@ParameterObject DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult<DishPageQueryVO> pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除")
    @DeleteMapping
    public Result deleteBatchByIds(@RequestParam List<Long> ids) {
        log.info("批量删除: {}", ids);
        dishService.deleteBatchByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Operation(summary = "根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishGetOneByIdVO> getOneById(@PathVariable Long id) {
        log.info("根据id查询菜品: {}", id);
        return Result.success(dishService.getOneById(id));
    }

    /**
     * 根据分类的id查询菜品
     * @param categoryId
     * @return
     */
    @Operation(summary = "根据分类的id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> listByCategoryId(Long categoryId) {
        log.info("根据分类的id查询菜品: {}", categoryId);
        return Result.success(dishService.listByCategoryId(categoryId));
    }

    /**
     * 修改菜品
     * @param dishSaveDTO
     * @return
     */
    @Operation(summary = "修改菜品")
    @PutMapping
    public Result updateWithFlavor(@RequestBody DishSaveDTO dishSaveDTO) {
        log.info("修改菜品: {}", dishSaveDTO);
        dishService.updateWithFlavor(dishSaveDTO);
        return Result.success();
    }

    /**
     * 菜品启售停售
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "菜品启售停售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("菜品启售停售: {}, {}", status, id);
        dishService.updateStatus(status, id);
        return Result.success();
    }
}

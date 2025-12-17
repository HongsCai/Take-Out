package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.SetmealPageQueryDTO;
import com.hongs.skycommon.pojo.dto.SetmealSaveDTO;
import com.hongs.skycommon.pojo.vo.SetmealGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.SetmealPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealSaveDTO
     * @return
     */
    @Operation(summary = "新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealSaveDTO setmealSaveDTO) {
        log.info("新增套餐: {}", setmealSaveDTO);
        setmealService.saveWithDish(setmealSaveDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Operation(summary = "套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult<SetmealPageQueryVO>> page(@ParameterObject SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询: {}", setmealPageQueryDTO);
        PageResult<SetmealPageQueryVO> pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除套餐")
    @DeleteMapping
    public Result deleteBatchByIds(@RequestParam List<Long> ids) {
        log.info("批量删除套餐: {}", ids);
        setmealService.deleteBatchByIds(ids);
        return Result.success();
    }

    @Operation(summary = "根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealGetOneByIdVO> getOneById(@PathVariable Long id) {
        log.info("根据id查询套餐: {}", id);
        SetmealGetOneByIdVO setmealGetOneByIdVO = setmealService.getOneById(id);
        return Result.success(setmealGetOneByIdVO);
    }

    /**
     * 修改套餐
     * @param setmealSaveDTO
     * @return
     */
    @Operation(summary = "修改套餐")
    @PutMapping
    public Result updateWithDish(@RequestBody SetmealSaveDTO setmealSaveDTO) {
        log.info("修改套餐: {}", setmealSaveDTO);
        setmealService.updateWithDish(setmealSaveDTO);
        return Result.success();
    }

    /**
     * 套餐启售停售
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "套餐启售停售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("套餐启售停售: {}, {}", status, id);
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}

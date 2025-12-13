package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.SetmealPageQueryDTO;
import com.hongs.skycommon.pojo.dto.SetmealSaveDTO;
import com.hongs.skycommon.pojo.vo.SetmealPageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
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
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealSaveDTO
     * @return
     */
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
     * 套餐起售停售
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "套餐起售停售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("套餐起售停售: {}, {}", status, id);
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}

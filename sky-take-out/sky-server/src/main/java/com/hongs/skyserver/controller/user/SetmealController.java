package com.hongs.skyserver.controller.user;

import com.hongs.skycommon.pojo.entity.Setmeal;
import com.hongs.skycommon.pojo.vo.SetmealWithDishGetByIdVO;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Tag(name = "套餐浏览接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Operation(summary = "根据分类id查询套餐")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("根据分类id查询套餐: {}", categoryId);
        List<Setmeal> setmealList = setmealService.listByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    @Operation(summary = "根据套餐id查询包含的菜品")
    @GetMapping("/dish/{id}")
    public Result<List<SetmealWithDishGetByIdVO>> getWithDishById(@PathVariable Long id) {
        log.info("根据套餐id查询包含的菜品: {}", id);
        List<SetmealWithDishGetByIdVO> setmealWithDishGetByIdVOList = setmealService.getWithDishById(id);
        return Result.success(setmealWithDishGetByIdVOList);
    }

}

package com.hongs.skyserver.controller.user;

import com.hongs.skycommon.pojo.vo.DishGetOneByIdVO;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@Tag(name = "菜品浏览接口")
@Slf4j
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Operation(summary = "根据分类ID查询菜品及口味")
    @GetMapping("/list")
    public Result<List<DishGetOneByIdVO>> listWithFlavorByCategoryId(Long categoryId) {
        log.info("根据分类ID查询菜品: {}", categoryId);
        List<DishGetOneByIdVO> dishList = dishService.listWithFlavorByCategoryId(categoryId);
        return Result.success(dishList);
    }
}

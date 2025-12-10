package com.hongs.skyserver.controller.admin;


import com.hongs.skycommon.pojo.dto.DishSaveDTO;
import com.hongs.skycommon.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品相关接口")
public class DishController {

    /**
     * 新增菜品
     * @param dishSaveDTO
     * @return
     */
    @Operation(summary = "新增菜品")
    @PostMapping
    public Result save(@RequestBody DishSaveDTO dishSaveDTO) {
        log.info("新增菜品: {}", dishSaveDTO);


        return Result.success();
    }

}

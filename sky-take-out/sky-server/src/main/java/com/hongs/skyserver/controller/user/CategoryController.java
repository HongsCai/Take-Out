package com.hongs.skyserver.controller.user;

import com.hongs.skycommon.pojo.entity.Category;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Slf4j
@Tag(name = "分类接口")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "条件查询")
    public Result list(Integer type) {
        log.info("条件查询: {}", type);
        List<Category> categoryList = categoryService.listByType(type);
        return Result.success(categoryList);
    }


}

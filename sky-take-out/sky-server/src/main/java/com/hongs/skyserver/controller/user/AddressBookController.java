package com.hongs.skyserver.controller.user;


import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.pojo.dto.AddressBookSaveDTO;
import com.hongs.skycommon.pojo.dto.AddressBookSetDefaultDTO;
import com.hongs.skycommon.pojo.entity.AddressBook;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/addressBook")
@Slf4j
@Tag(name = "地址簿接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @Operation(summary = "新增地址")
    @PostMapping
    public Result save(@RequestBody AddressBookSaveDTO addressBookSaveDTO) {
        log.info("新增地址: {}", addressBookSaveDTO);
        addressBookService.save(addressBookSaveDTO);
        return Result.success();
    }

    @Operation(summary = "查询当前登录用户的所有地址信息")
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        log.info("查询所有地址信息-用户: {}", BaseContext.getCurrentId());
        return Result.success(addressBookService.listByUserId());
    }

    @Operation(summary = "查询当前登录用户的默认地址")
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址-用户: {}", BaseContext.getCurrentId());
        return Result.success(addressBookService.getDefaultByUserId());
    }

    @Operation(summary = "根据id查询地址")
    @GetMapping("/{id}")
    public Result<AddressBook> getOne(@PathVariable Long id) {
        log.info("查询地址-ID: {}", id);
        return Result.success(addressBookService.getById(id));
    }

    @Operation(summary = "根据id修改地址")
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址-ID: {}", addressBook);
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    // TODO 后期有空重写前端请求地址 这里先必须加上 "/"
    @Operation(summary = "根据id删除地址")
    @DeleteMapping("/")
    public Result deleteById(@RequestParam Long id) {
        log.info("删除地址-ID: {}", id);
        addressBookService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBookSetDefaultDTO addressBookSetDefaultDTO) {
        log.info("设置默认地址-ID: {}", addressBookSetDefaultDTO);
        addressBookService.setDefault(addressBookSetDefaultDTO.getId());
        return Result.success();
    }

}

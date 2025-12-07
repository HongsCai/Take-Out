package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.*;
import com.hongs.skycommon.pojo.entity.Employee;
import com.hongs.skycommon.pojo.vo.EmployeeGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skycommon.pojo.vo.EmployeePageQueryVO;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.result.Result;
import com.hongs.skyserver.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Operation(summary = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录: {}", employeeLoginDTO);
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    /**
     * 员工退出
     *
     * @return
     */
    @Operation(summary = "员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @Operation(summary = "新增员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param pageQueryDTO
     * @return
     */
    @Operation(summary = "员工分页查询")
    @GetMapping("/page")
    public Result<PageResult<EmployeePageQueryVO>> page(@ParameterObject EmployeePageQueryDTO pageQueryDTO) {
        log.info("员工分页查询: {}", pageQueryDTO);
        PageResult<EmployeePageQueryVO> pageResult = employeeService.page(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     *
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "启用禁用员工账号")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号: {}, {}", status, id);
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询员工
     *
     * @param id
     * @return
     */
    @Operation(summary = "根据ID查询员工")
    @GetMapping("/{id}")
    public Result<EmployeeGetOneByIdVO> getOneById(@PathVariable Long id) {
        log.info("根据ID查询员工: {}", id);
        EmployeeGetOneByIdVO employeeGetOneByIdVO = employeeService.getOneById(id);
        return Result.success(employeeGetOneByIdVO);
    }

    /**
     * 修改员工信息
     *
     * @param employeeUpdateInfoDTO
     * @return
     */
    @Operation(summary = "修改员工信息")
    @PutMapping
    public Result updateInfo(@RequestBody EmployeeUpdateInfoDTO employeeUpdateInfoDTO) {
        log.info("修改员工信息: {}", employeeUpdateInfoDTO);
        employeeService.updateInfo(employeeUpdateInfoDTO);
        return Result.success();
    }

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     * @return
     */
    @Operation(summary = "修改密码")
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        log.info("修改密码: {}", employeeEditPasswordDTO);
        employeeService.editPassword(employeeEditPasswordDTO);
        return Result.success();
    }
}

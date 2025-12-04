package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.pojo.dto.EmployeeLoginDTO;
import com.hongs.skycommon.pojo.entity.Employee;
import com.hongs.skycommon.properties.JwtProperties;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skyserver.common.BaseResponse;
import com.hongs.skyserver.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public BaseResponse<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录: {}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成果生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();


    }


}

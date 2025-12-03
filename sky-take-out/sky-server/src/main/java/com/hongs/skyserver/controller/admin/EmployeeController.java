package com.hongs.skyserver.controller.admin;

import com.hongs.skycommon.properties.JwtProperties;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skyserver.common.BaseResponse;
import com.hongs.skyserver.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<EmployeeLoginVO> login() {

    }


}

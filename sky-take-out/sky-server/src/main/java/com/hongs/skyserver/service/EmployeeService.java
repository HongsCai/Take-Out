package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.EmployeeLoginDTO;
import com.hongs.skycommon.pojo.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Hongs
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2025-12-02 23:26:20
*/
public interface EmployeeService extends IService<Employee> {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}

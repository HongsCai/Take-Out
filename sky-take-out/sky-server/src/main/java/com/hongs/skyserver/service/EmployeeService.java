package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.EmployeeDTO;
import com.hongs.skycommon.pojo.dto.EmployeeLoginDTO;
import com.hongs.skycommon.pojo.dto.EmployeePageQueryDTO;
import com.hongs.skycommon.pojo.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skycommon.pojo.vo.EmployeePageQueryVO;
import com.hongs.skycommon.result.PageResult;

/**
* @author Hongs
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2025-12-02 23:26:20
*/
public interface EmployeeService extends IService<Employee> {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult<EmployeePageQueryVO> page(EmployeePageQueryDTO pageQueryDTO);
}

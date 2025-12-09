package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.*;
import com.hongs.skycommon.pojo.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.EmployeeGetOneByIdVO;
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
     * @param employeeSaveDTO
     * @return
     */
    void save(EmployeeSaveDTO employeeSaveDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult<EmployeePageQueryVO> page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    EmployeeGetOneByIdVO getOneById(Long id);

    /**
     * 修改员工信息
     * @param employeeUpdateInfoDTO
     * @return
     */
    void updateInfo(EmployeeUpdateInfoDTO employeeUpdateInfoDTO);

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     * @return
     */
    void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO);
}

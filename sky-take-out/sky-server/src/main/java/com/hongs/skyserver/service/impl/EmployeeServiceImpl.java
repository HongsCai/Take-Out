package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.exception.AccountLockedException;
import com.hongs.skycommon.exception.AccountNotFoundException;
import com.hongs.skycommon.exception.PasswordErrorException;
import com.hongs.skycommon.pojo.dto.EmployeeLoginDTO;
import com.hongs.skycommon.pojo.entity.Employee;
import com.hongs.skyserver.service.EmployeeService;
import com.hongs.skyserver.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author Hongs
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2025-12-02 23:26:20
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

    /**
     *
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        Employee employee = this.getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, username));

        // 用户不存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码对比
        // TODO 后期使用MD5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 用户账号状态检测
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }
}





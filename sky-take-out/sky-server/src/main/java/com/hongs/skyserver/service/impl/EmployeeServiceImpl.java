package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.JwtClaimsConstant;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.constant.PasswordConstant;
import com.hongs.skycommon.constant.StatusConstant;
import com.hongs.skycommon.context.BaseContext;
import com.hongs.skycommon.exception.AccountLockedException;
import com.hongs.skycommon.exception.AccountNotFoundException;
import com.hongs.skycommon.exception.PasswordEditFailedException;
import com.hongs.skycommon.exception.PasswordErrorException;
import com.hongs.skycommon.pojo.dto.*;
import com.hongs.skycommon.pojo.entity.Employee;
import com.hongs.skycommon.pojo.vo.EmployeeGetOneByIdVO;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skycommon.pojo.vo.EmployeePageQueryVO;
import com.hongs.skycommon.properties.JwtProperties;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.utils.JwtUtil;
import com.hongs.skyserver.mapper.EmployeeMapper;
import com.hongs.skyserver.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Hongs
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2025-12-02 23:26:20
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        Employee employee = this.getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, username));

        // 用户不存在
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码对比（进行MD5加密，再比对）
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 用户账号状态检测
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 登录成果生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return employeeLoginVO;
    }

    /**
     * 新增员工
     * @param employeeSaveDTO
     * @return
     */
    @Override
    public void save(EmployeeSaveDTO employeeSaveDTO) {
        /**
         * 拷贝DTO属性
         * 设置账号状态
         * 设置密码为默认密码
         * 设置创建时间和修改时间
         * 设置为当前创建人id和修改人的id
         */
        Employee employee = Employee.builder()
                .id(employeeSaveDTO.getId())
                .username(employeeSaveDTO.getUsername())
                .name(employeeSaveDTO.getName())
                .sex(employeeSaveDTO.getSex())
                .idNumber(employeeSaveDTO.getIdNumber())
                .phone(employeeSaveDTO.getPhone())
                .status(StatusConstant.DISABLE)
                .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()))
                .build();
        this.save(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult<EmployeePageQueryVO> page(EmployeePageQueryDTO employeePageQueryDTO) {
        IPage<Employee> iPage = new Page(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(employeePageQueryDTO.getName()), Employee::getName, employeePageQueryDTO.getName())
                .orderByDesc(Employee::getUpdateTime)
                .orderByDesc(Employee::getCreateTime);
        this.page(iPage, wrapper);

        List<EmployeePageQueryVO> employeePageQueryVOList = iPage.getRecords().stream().map(employee -> {
            EmployeePageQueryVO employeePageQueryVO = new EmployeePageQueryVO();
            BeanUtils.copyProperties(employee, employeePageQueryVO);
            return employeePageQueryVO;
        }).toList();

        return new PageResult<EmployeePageQueryVO>(iPage.getTotal(), employeePageQueryVOList);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        this.updateById(employee);
    }

    /**
     * 根据ID查询员工
     * @param id
     * @return
     */
    @Override
    public EmployeeGetOneByIdVO getOneById(Long id) {
        Employee employee = this.getById(id);
        EmployeeGetOneByIdVO employeeGetOneByIdVO = new EmployeeGetOneByIdVO();
        BeanUtils.copyProperties(employee, employeeGetOneByIdVO);
        return employeeGetOneByIdVO;
    }

    /**
     * 修改员工信息
     * @param employeeUpdateInfoDTO
     * @return
     */
    @Override
    public void updateInfo(EmployeeUpdateInfoDTO employeeUpdateInfoDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeUpdateInfoDTO, employee);
        this.updateById(employee);
    }

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     * @return
     */
    @Override
    public void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        Long currentId = BaseContext.getCurrentId();
        Employee employee = this.getById(currentId);

        String oldPasswordHash = DigestUtils.md5DigestAsHex(employeeEditPasswordDTO.getOldPassword().getBytes());
        if (!employee.getPassword().equals(oldPasswordHash)) {
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_ERROR);
        }

        Employee updateEmployee = Employee.builder()
                .id(currentId)
                .password(DigestUtils.md5DigestAsHex(employeeEditPasswordDTO.getNewPassword().getBytes()))
                .build();

        this.updateById(updateEmployee);
    }
}

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
import com.hongs.skycommon.exception.PasswordErrorException;
import com.hongs.skycommon.pojo.dto.EmployeeDTO;
import com.hongs.skycommon.pojo.dto.EmployeeLoginDTO;
import com.hongs.skycommon.pojo.dto.EmployeePageQueryDTO;
import com.hongs.skycommon.pojo.entity.Employee;
import com.hongs.skycommon.pojo.vo.EmployeeLoginVO;
import com.hongs.skycommon.pojo.vo.EmployeePageQueryVO;
import com.hongs.skycommon.properties.JwtProperties;
import com.hongs.skycommon.result.PageResult;
import com.hongs.skycommon.utils.JwtUtil;
import com.hongs.skyserver.service.EmployeeService;
import com.hongs.skyserver.mapper.EmployeeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return employeeLoginVO;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号状态
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码为默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置为当前创建人id和修改人的id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        this.save(employee);
    }

    /**
     * 员工分页查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult<EmployeePageQueryVO> page(EmployeePageQueryDTO pageQueryDTO) {
        IPage<Employee> iPage = new Page(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Employee::getUpdateTime)
                .orderByDesc(Employee::getCreateTime);
        wrapper.like(StringUtils.hasText(pageQueryDTO.getName()), Employee::getName, pageQueryDTO.getName());
        this.page(iPage, wrapper);

        List<EmployeePageQueryVO> employeePageQueryVOList = iPage.getRecords().stream().map(employee -> {
            EmployeePageQueryVO employeePageQueryVO = new EmployeePageQueryVO();
            BeanUtils.copyProperties(employee, employeePageQueryVO);
            return employeePageQueryVO;
        }).toList();

        return new PageResult<EmployeePageQueryVO>(iPage.getTotal(), employeePageQueryVOList);
    }
}





package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.entity.Employee;
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

}





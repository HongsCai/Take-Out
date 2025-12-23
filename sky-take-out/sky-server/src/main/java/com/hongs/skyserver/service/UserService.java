package com.hongs.skyserver.service;

import com.hongs.skycommon.pojo.dto.UserLoginDTO;
import com.hongs.skycommon.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongs.skycommon.pojo.vo.UserLoginVO;

/**
* @author Hongs
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2025-12-23 15:16:47
*/
public interface UserService extends IService<User> {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

}

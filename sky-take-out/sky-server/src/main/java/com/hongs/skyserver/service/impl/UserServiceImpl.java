package com.hongs.skyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongs.skycommon.constant.JwtClaimsConstant;
import com.hongs.skycommon.constant.MessageConstant;
import com.hongs.skycommon.exception.LoginFailedException;
import com.hongs.skycommon.pojo.dto.UserLoginDTO;
import com.hongs.skycommon.pojo.entity.User;
import com.hongs.skycommon.pojo.vo.UserLoginVO;
import com.hongs.skycommon.properties.JwtProperties;
import com.hongs.skycommon.properties.WechatProperties;
import com.hongs.skycommon.utils.HttpClientUtil;
import com.hongs.skycommon.utils.JwtUtil;
import com.hongs.skyserver.mapper.UserMapper;
import com.hongs.skyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author Hongs
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2025-12-23 15:16:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private WechatProperties wechatProperties;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        Map<String, Object> param = Map.of("appid", wechatProperties.getAppId(),
                "secret", wechatProperties.getAppSecret(),
                "js_code", userLoginDTO.getCode(),
                "grant_type", "authorization_code"
        );
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String json = HttpClientUtil.doGet(url, param);
        Map<String, Object> map = HttpClientUtil.jsonToMap(json);
        String openId = (String) map.get("openid");

        if (openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openId));
        if (user == null) {
            user = User.builder().openid(openId).build();
            this.save(user);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
    }
}





package com.liangjing.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.liangjing.pojo.User;
import com.liangjing.service.UserService;
import com.liangjing.util.JWTUtil;
import com.liangjing.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hewei
 * @date 2022/8/17 14:29
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public String LoginJWT(User user) {
        HashMap<String, String> map = new HashMap<>();
        user.setId(1);
        //获取用户信息
        //验证用户信息
        //生成jwt
        map.put("id",user.getId().toString());
        map.put("username",user.getUsername());
        String token = JWTUtil.getToken(map);
        redisUtil.set(user.getId().toString(),token,60*30);
        return token;
    }

    @Override
    public Map getInfo(String id) {
        HashMap<Object, Object> result = new HashMap<>();
        String token = (String) redisUtil.get(id);
        DecodedJWT decode = JWTUtil.decode(token);
        result.put("token",token);
        result.put("id",decode.getClaim("id").asString());
        result.put("name",decode.getClaim("username").asString());
        long ex = redisUtil.getExpire(decode.getClaim("id").asString());
        result.put("exp",ex);
        return result;
    }
}

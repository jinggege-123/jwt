package com.liangjing.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.liangjing.util.JWTUtil;
import com.liangjing.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hewei
 * @date 2022/8/17 14:14
 */


public class AuthenticateInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
//        String token = authToken.substring("Bearer".length() + 1).trim();
        DecodedJWT decode = JWTUtil.decode(token);
        String id = decode.getClaim("id").asString();
        //1.判断请求是否有效
        if (redisUtil.get(id) == null
                || !redisUtil.get(id).equals(token)) {
            return false;
        }

        //2.判断是否需要续期
        if (redisUtil.getExpire(id) < 1 * 60 * 28) {
            redisUtil.set(id, token,60*30);
            log.error("update token info, id is:{}, user info is:{}", id, token);
        }
        return true;
    }
}

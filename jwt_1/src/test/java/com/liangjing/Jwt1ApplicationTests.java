package com.liangjing;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.liangjing.pojo.User;
import com.liangjing.pojo.UserVo;
import com.liangjing.util.JWTUtil;
import com.liangjing.util.RedisUtil;
import io.netty.handler.codec.base64.Base64Decoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;

@SpringBootTest
class Jwt1ApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() throws IllegalAccessException {
        User user = new User(1,"test","123456",12,"1");
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        HashMap<String, String> map = new HashMap<>();
        Field[] fields = userVo.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(),  field.get(userVo).toString());
        }
//        map.put("id",userVo.getId().toString());
//        map.put("username",userVo.getUsername());
        String token = JWTUtil.getToken(map);
        System.out.println("token:***"+token);
        redisUtil.set(userVo.getId().toString(),token,60);
//        DecodedJWT decode = JWTUtil.decode(token);
//        System.out.println(decode.getClaim("id").asString()+"**"+decode.getClaim("username").asString());
    }

    @Test
    public void test()  {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InRlc3QifQ.bxAdkieAnBnLkk8RDyA_lnncxQva3D6-LNMgByj05j4";
        DecodedJWT decode = JWTUtil.decode(token);
        String id = decode.getClaim("id").asString();
        String redisToken = (String) redisUtil.get(id);
        System.out.println(redisToken);
        if(token.equals(redisToken)){
            System.out.println("身份合法！");
        }else{
            System.out.println("身份不合法！");
            return;
        }

        long expire = redisUtil.getExpire(id);
        System.out.println("剩余时长："+expire);
        if(expire<30){
            redisUtil.set(id,token,60);
        }
        long expire2 = redisUtil.getExpire(id);
        System.out.println("新增剩余时长："+expire2);

    }

}

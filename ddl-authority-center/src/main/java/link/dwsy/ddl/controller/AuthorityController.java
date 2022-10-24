package link.dwsy.ddl.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import link.dwsy.ddl.XO.RB.UserRB;
import link.dwsy.ddl.XO.RB.UserRegisterRB;
import link.dwsy.ddl.annotation.IgnoreResponseAdvice;
import link.dwsy.ddl.constant.AuthorityConstant;
import link.dwsy.ddl.core.CustomExceptions.CodeException;
import link.dwsy.ddl.core.constant.CustomerErrorCode;
import link.dwsy.ddl.core.constant.TokenConstants;
import link.dwsy.ddl.core.domain.JwtToken;
import link.dwsy.ddl.core.utils.RSAUtil;
import link.dwsy.ddl.service.impl.TokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <h1>对外暴露的授权服务接口</h1>
 */
@Slf4j
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Resource
    private TokenServiceImpl tokenService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;


    @GetMapping("/rsa-pks")
    public String getRsaPublicKey() {

        return RSAUtil.getPublicKeyStr();
    }

    /**
     * <h2>从授权中心获取 Token (其实就是登录功能), 且返回信息中没有统一响应的包装</h2>
     */
    @IgnoreResponseAdvice
    @PostMapping("/token")
    public JwtToken token(@RequestBody UserRB userRB)
            throws Exception {

        log.info("request to get token with param: [{}]",
                JSONUtil.toJsonStr(userRB));
        JwtToken jwtToken = new JwtToken(tokenService.generateToken(userRB));
        redisTemplate.opsForValue().set(TokenConstants.REDIS_TOKEN_ACTIVE_TIME_KEY + jwtToken.getToken(), String.valueOf(new Date().getTime()),
                AuthorityConstant.DEFAULT_EXPIRE_DAY, TimeUnit.DAYS);
        return jwtToken;
    }

    @PostMapping("/active")
    public boolean active(HttpServletRequest request) {
        String tokenHead = request.getHeader(TokenConstants.AUTHENTICATION);
        return tokenService.active(tokenHead.split(" ")[1]);
    }

    @PostMapping("logout")
    public boolean logout(HttpServletRequest request) {
        String tokenHead = request.getHeader(TokenConstants.AUTHENTICATION);
        if (StrUtil.isBlank(tokenHead)) {
            return false;
        }
        tokenService.blackToken(tokenHead.split(" ")[1]);
        request.getSession().invalidate();
        return true;
    }

    @PostMapping("refresh")
    @IgnoreResponseAdvice
    public String refresh(HttpServletRequest request) throws Exception {
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        if (StrUtil.isBlank(token)) {
            throw new CodeException(CustomerErrorCode.TokenNotFound);
        }
        request.getSession().invalidate();
        return tokenService.refreshToken(token);
    }

    /**
     * <h2>注册用户并返回当前注册用户的 Token, 即通过授权中心创建用户</h2>
     */
    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UserRegisterRB userRegisterRB)
            throws Exception {

        log.info("register user with param: [{}]", JSONUtil.toJsonStr(
                userRegisterRB
        ));
        return new JwtToken(tokenService.registerUserAndGenerateToken(
                userRegisterRB
        ));
    }
}

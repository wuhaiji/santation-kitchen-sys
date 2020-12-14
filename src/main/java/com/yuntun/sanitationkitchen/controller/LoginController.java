package com.yuntun.sanitationkitchen.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.constant.UserConstant;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.User;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.vo.UserLoginVo;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.yuntun.sanitationkitchen.service.IUserService;
import com.yuntun.sanitationkitchen.auth.AuthUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.RSAUtils;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static com.yuntun.sanitationkitchen.constant.UserConstant.RSA_KEYPAIR_REDIS_KEY;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@RestController
@RequestMapping("/open")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    public static final int FIVE_MINUTE = 300;

    @Autowired
    IUserService iUserService;


    @Autowired
    private IPoundBillService poundBillService;


    @PostMapping("/login")
    public Result<Object> login(
            String username,
            String password,
            String code,
            String publickey,
            String captchaId
    ) {

        ErrorUtil.isStringEmpty(code, "图形验证码");
        ErrorUtil.isStringEmpty(publickey, "密匙");
        ErrorUtil.isStringEmpty(password, "密码");
        ErrorUtil.isStringEmpty(username, "账号");

        //先验证图形验证码
        String targetCode = RedisUtils.getString(UserConstant.CAPTCHA_ID_REDIS_KEY + captchaId);
        if (!code.equals(targetCode)) {
            return Result.error(UserCode.LOGIN_CAPTCHA_ERROR);
        }
        //再验证账号
        User targetUser;
        try {
            targetUser = iUserService.getOne(
                    new QueryWrapper<User>()
                            .eq("username", username)
                            .or()
                            .eq("phone", username)
            );
        } catch (Exception e) {
            log.error("login error:", e);
            throw new ServiceException(UserCode.LOGIN_EXCEPTION);
        }

        if (targetUser == null) {
            return Result.error(UserCode.LOGIN_FAILED_USERNAME_INCORRECT);
        }

        if (targetUser.getDisabled().equals(UserConstant.IS_DISABLED)) {
            return Result.error(UserCode.LOGIN_FAILED_ERROR_ACCOUNT_IS_DISABLED);
        }

        //最后验证密码
        String passwordEncrypt = getPasswordDecrypt(password, publickey);
        if (!BCrypt.checkpw(passwordEncrypt, targetUser.getPassword())) {
            return Result.error(UserCode.LOGIN_FAILED_PASSWORD_INCORRECT);
        }

        //生成 TokenBody
        AuthUtil.TokenInfo tokenInfo;
        try {
            tokenInfo = AuthUtil.generalToken(targetUser.getUid());
        } catch (Exception e) {
            log.error("生成token异常:", e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }

        //更新用户登录时间
        targetUser.setLastLoginTime(LocalDateTime.now());
        iUserService.updateById(targetUser);

        //返回vo
        UserLoginVo userLoginVo = new UserLoginVo()
                .setPhone(targetUser.getPhone())
                .setUsername(targetUser.getUsername())
                .setUserId(targetUser.getUid())
                .setTokenInfo(tokenInfo);
        return Result.ok(userLoginVo);
    }

    @PostMapping("/refresh/token")
    public Result<Object> getRoomDeviceList(String refreshToken) {
        System.out.println(refreshToken.length());
        ErrorUtil.isStringLengthOutOfRange(refreshToken, 0, 32, "刷新token");
        AuthUtil.TokenInfo tokenInfo;
        try {
            tokenInfo = AuthUtil.refreshToken(refreshToken);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }
        if (tokenInfo == null) {
            throw new ServiceException(UserCode.REFRESH_TOKEN_EXPIRED);
        }
        return Result.ok(tokenInfo);
    }

    public static void main(String[] args) {
        // String hashpw = BCrypt.hashpw("123456");
        // System.out.println(hashpw);
        // System.out.println(BCrypt.checkpw("123456", hashpw));
        long l = IdUtil.getSnowflake(1, 1).nextId();
        System.out.println(l);
    }



    /**
     * 获取public key
     *
     * @return 公钥
     */
    @GetMapping("/publickey")
    public Result<String> getPublicKey() {
        long timeMillis1 = System.currentTimeMillis();
        Map<String, String> map = RSAUtils.genKeyPair();
        long timeMillis2 = System.currentTimeMillis();
        System.out.println("生成密匙对时间：" + (timeMillis2 - timeMillis1));
        String publicKey = map.get(RSAUtils.PUBLIC_KEY_STR);
        String privateKey = map.get(RSAUtils.PRIVATE_KEY_STR);
        //5分钟过期
        RedisUtils.setValueExpireSeconds(RSA_KEYPAIR_REDIS_KEY + SecureUtil.md5(publicKey), privateKey, 300);
        return Result.ok(publicKey);
    }

    /**
     * 获取验证码
     *
     * @return 图形验证码图片base64
     */
    @GetMapping("/captcha")
    public Result<Object> captcha() throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String code = captcha.getCode();
        log.info("验证码：{}", code);
        String imageBase64 = captcha.getImageBase64();
        String captchaId = AuthUtil.getRandomString(16);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("base64Img", imageBase64);
        jsonObject.put("captchaId", captchaId);
        //5分钟
        //Handheld
        RedisUtils.setValueExpireSeconds(UserConstant.CAPTCHA_ID_REDIS_KEY + captchaId, code, FIVE_MINUTE);
        return Result.ok(jsonObject);
    }
    @GetMapping("/uid")
    public Result<Object> uid() {
        return Result.ok(SnowflakeUtil.getUnionId());
    }

    @PostMapping("/password/update")
    public Result<Object> update(String username, String oldPassword, String newPassword, String publickey) {

        ErrorUtil.isStringEmpty(username, "用户名");

        ErrorUtil.isStringEmpty(publickey, "公钥");

        //先查询用户是否存在
        User targetUser = iUserService.getOne(new QueryWrapper<User>().eq("username", username));
        if (targetUser == null) {
            log.error("用户不存在");
            throw new ServiceException(UserCode.LOGIN_FAILED_USERNAME_INCORRECT);
        }

        //判断旧密码是否正确
        String passwordDecrypt = LoginController.getPasswordDecrypt(oldPassword, publickey);
        if (!BCrypt.checkpw(passwordDecrypt, targetUser.getPassword())) {
            log.error("密码不正确");
            throw new ServiceException(UserCode.LOGIN_FAILED_PASSWORD_INCORRECT);
        }

        //解密新密码
        String newPasswordDecrypt = LoginController.getPasswordDecrypt(newPassword, publickey);
        ErrorUtil.isStringLengthOutOfRange(newPasswordDecrypt, 6, 16, "新密码");
        String newPasswordMd5 = BCrypt.hashpw(newPasswordDecrypt);
        targetUser.setPassword(newPasswordMd5);
        boolean save = iUserService.update(targetUser, new QueryWrapper<User>().eq("uid", targetUser.getUid()));
        if (save)
            return Result.ok();
        return Result.error(UserCode.PASSWORD_UPDATE_ERROR);

    }

    /**
     * 通过redis解密密码
     *
     * @param password
     * @param publicKey
     * @return
     */
    public static String getPasswordDecrypt(String password, String publicKey) {
        String privateKey = RedisUtils.getString(RSA_KEYPAIR_REDIS_KEY + SecureUtil.md5(publicKey));
        if (privateKey == null) {
            throw new ServiceException(UserCode.LOGIN_FAILED_PUBLICKEY_INCORRECT);
        }
        //解密
        String passwordDecrypt;
        try {
            passwordDecrypt = RSAUtils.decrypt(password, privateKey);
        } catch (Exception e) {
            log.error("解密错误：", e);
            //解密出錯，返回登录异常
            throw new ServiceException(UserCode.LOGIN_FAILED_PUBLICKEY_INCORRECT);
        }
        return passwordDecrypt;
    }


}

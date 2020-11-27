package com.yuntun.sanitationkitchen.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * AES加密工具类
 *
 * @author vic
 */
public class AESUtil {
    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    /**
     * 秘钥的大小
     */
    private static final int KEY_SIZE = 128;

    /**
     * AES加密
     *
     * @param data 待加密内容
     * @param key  加密秘钥
     * @return
     */
    public static byte[] encrypt(String data, String key) {
        if (EptUtil.isNotEmpty(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEY_SIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = data.getBytes(StandardCharsets.UTF_8);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
                return cipher.doFinal(byteContent); // 加密
            } catch (Exception e) {
                log.error("Exception:",e);
            }
        }
        return null;
    }

    /**
     * AES加密，返回String
     *
     * @param data 待加密内容
     * @param key  加密秘钥
     * @return
     */
    public static String encryptToStr(String data, String key) {
        return EptUtil.isNotEmpty(data) ? parseByte2HexStr(Objects.requireNonNull(encrypt(data, key))) : null;
    }

    /**
     * AES解密
     *
     * @param data 待解密字节数组
     * @param key  秘钥
     * @return
     */
    public static byte[] decrypt(byte[] data, String key) {
        if (EptUtil.isNotEmpty(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEY_SIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
                return cipher.doFinal(data); // 加密
            } catch (Exception e) {
                log.error("Exception:",e);
            }
        }
        return null;
    }

    /**
     * AES解密，返回String
     *
     * @param enCryptdata 待解密字节数组
     * @param key         秘钥
     * @return
     */
    public static String decryptToStr(String enCryptdata, String key) {
        return EptUtil.isNotEmpty(enCryptdata) ? new String(Objects.requireNonNull(decrypt(parseHexStr2Byte(enCryptdata), key))) : null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf 二进制数组
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}

package com.liuzz.cloud.common.core.utils.encrypt;

import cn.hutool.core.util.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加解密工具
 *
 * @author lzz
 */
public class AesUtil {

    public static final String AES = "AES";

    public static final String AES_CBC_CIPHER = "AES/CBC/PKCS5Padding";

    public static final String KEY = "BgGCHVJB6EStiLat";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密,加密
     *
     * @param data 需要加密的内容
     * @return
     */
    public static String encrypt(String data) {
        byte[] valueByte = encrypt(data.getBytes(StandardCharsets.UTF_8), KEY.getBytes(StandardCharsets.UTF_8));
        return HexUtil.encodeHexStr(valueByte);
    }

    /**
     * 解密
     *
     * @param data 待解密内容字符串
     * @return
     */
    public static String decrypt(String data) {
        byte[] originalData = HexUtil.decodeHex(data);
        byte[] valueByte = decrypt(originalData, KEY.getBytes(StandardCharsets.UTF_8));
        return new String(valueByte);
    }


}

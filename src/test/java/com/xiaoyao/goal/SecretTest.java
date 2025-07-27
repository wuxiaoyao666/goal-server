package com.xiaoyao.goal;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * @author 逍遥
 */
public class SecretTest {
    private static final String Secret = "SengeSenge520...";
    private static final AES aes = SecureUtil.aes(Secret.getBytes());

    public static void main(String[] args) {
        String content = "test中文哈哈哈哈哈哈";

        // 随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

        // 构建
        //AES aes = new AES(Mode.CTS, Padding.NoPadding,"0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());
        //AES aes = SecureUtil.aes("SengeSenge520...".getBytes());
        // 加密
        byte[] encrypt = aes.encrypt(content);
        // 解密
        String decrypt = aes.decryptStr(encrypt);
        System.out.println(decrypt);
        // 加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
    }
}

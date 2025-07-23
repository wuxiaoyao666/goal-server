package com.xiaoyao.goal.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author 逍遥
 */
public class BcryptUtils {

    /**
     * 加密
     * @param plainPassword 明文密码
     * @return 加密后的密文
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * 校验密码
     * @param plainPassword 明文密码
     * @param storedHash 密文密码
     * @return 是否匹配
     */
    public static boolean verify(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}

package com.xiaoyao.flow;

import com.xiaoyao.flow.utils.BcryptUtils;

/**
 * @author 逍遥
 */
public class PwdTest {
    public static void main(String[] args) {
        String plainPassword = "senge520";
        String pwd = BcryptUtils.hash(plainPassword);
        System.out.println(pwd);
        System.out.println(BcryptUtils.verify(plainPassword, pwd));
        System.out.println(BcryptUtils.verify("sen", pwd));
    }
}

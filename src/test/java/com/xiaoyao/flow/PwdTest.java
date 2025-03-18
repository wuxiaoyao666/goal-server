package com.xiaoyao.flow;

import com.xiaoyao.flow.utils.BcryptUtils;

/**
 * @author 逍遥
 */
public class PwdTest {
    public static void main(String[] args) {
        String pwd = BcryptUtils.hash("senge520");
        System.out.println(pwd);
    }
}

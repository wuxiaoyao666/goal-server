package com.xiaoyao.goal.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 逍遥
 */
public class BlindGenerator {

    /**
     * 生成盲索引
     *
     * @param content 内容
     * @return 盲索引
     */
    public static String segment(String content) {
        Set<String> wordSet = new HashSet<>();
        List<Term> terms = HanLP.segment(content);
        terms.forEach(term -> {
            String word = term.toString().substring(0, term.length());
            if (StrUtil.isNotBlank(word)) wordSet.add(hashToken(word));
        });
        return String.join(" ", wordSet);
    }

    /**
     * 数据加密
     *
     * @param content 内容
     * @return 加密后的数据
     */
    public static String hashToken(String content) {
        return SecureUtil.md5(content);
    }
}

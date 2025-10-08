package com.xiaoyao.goal;

import cn.hutool.core.util.StrUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 逍遥
 */
public class HanlpTest {
    public static void main(String[] args) {
        String content = "第十一篇日记";
        Set<String> wordSet = new HashSet<>();
        List<Term> terms = HanLP.segment(content);
        terms.forEach(term -> {
            String word = term.word;
            if (StrUtil.isNotBlank(word)) wordSet.add(word);
        });
        System.out.println(wordSet);
        String keyword = "金铲铲";
        System.out.println(wordSet.contains(keyword));
    }
}

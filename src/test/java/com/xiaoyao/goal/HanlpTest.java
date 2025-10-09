package com.xiaoyao.goal;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 逍遥
 */
public class HanlpTest {
    private static final Set<String> StopNatures = Set.of("w", "f", "p", "c", "y", "o", "q");
    public static void main(String[] args) {
        String content = "这几天都有雨，还是决定骑车上班，早上迟到了十五分钟，到公司之后鞋子里都是水。\n" +
                " \n" +
                "Goal 进行了一些优化：\n" +
                "1. 点击日记再点击时间统计管理标签之后，页面背景会变为紫色；\n" +
                "2. 新建日记报错；\n" +
                "3. 优化部分页面样式；\n" +
                "4. 写日记保存后，进入timing，再进入diary返回来，会发现日记不是保存后的，需要点击其它日记再点击当前日记才能更新。\n" +
                "\n" +
                "Eagle_S\n" +
                "1. Eagle_S 关闭 Tau 修正计算误差\n" +
                "1.1. 先是整理了一遍完整计算；\n" +
                "1.2. 对方说是因为分母（Matrix）没有关闭 Tau 修正，经过我的排查 Matrix 插值计算取得点是 RawData 前的，和 Tau 计算无关。\n" +
                "\n" +
                "趁着雨停了，赶紧把自行车送到晨姐店里做一个小保养。";
        Set<String> wordSet = new HashSet<>();
        List<Term> terms = HanLP.segment(content);
        terms.forEach(term -> {
            String word = term.word;
            String nature = term.nature.toString();
            if(StrUtil.isNotBlank(word) && StrUtil.isNotBlank(nature) && !StopNatures.contains(nature)){
                System.out.printf("%s %s%n", nature, word);
                wordSet.add(word);
            }
        });
        System.out.println(JSONUtil.toJsonStr(wordSet));
        System.out.println(wordSet.size());
    }
}

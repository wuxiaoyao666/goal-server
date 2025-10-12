package com.xiaoyao.goal;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.util.HtmlUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 逍遥
 */
public class HanlpTest {
    private static final Set<String> StopNatures = Set.of("w", "f", "p", "c", "y", "o", "q");
    public static void main(String[] args) {
        String content = "<div class=\"lake-content\" typography=\"classic\"><p id=\"u09aa4863\" class=\"ne-p\"><span class=\"ne-text\">Goal 做了一些优化</span></p><p id=\"u3be878ba\" class=\"ne-p\"><span class=\"ne-text\">1. 早上到公司发现日记框居然可以下拉，于是乎就用 Element-Plus 的 Tag 组件优化了一下，虽然 Focus 后边框是蓝色的，不过也可以接受哈哈哈哈哈。耗时 22 分 30秒。</span></p><p id=\"u321e2aac\" class=\"ne-p\"><br></p><p id=\"u99c0dea6\" class=\"ne-p\"><span class=\"ne-text\">工作</span></p><p id=\"ucf9aeaa7\" class=\"ne-p\"><span class=\"ne-text\">电脑太卡，利用中午时间装了个系统，电脑是 i7 6代的 cpu，居然不能装 win11。下午两点四十左右装完，还是很卡，最终决定格式化电脑了。</span></p><p id=\"u713d68fa\" class=\"ne-p\"><br></p><p id=\"ub77dd5be\" class=\"ne-p\"><span class=\"ne-text\">Eagle_S</span></p><p id=\"u8e4435ac\" class=\"ne-p\"><span class=\"ne-text\">1. Matrix 的插值计算，不管是否开启 Tau 都要基于最新 RawData 后的曲线做计算，上午确定好需求，本来打算中午之前搞定的，结果公司电脑太卡了，于是乎装了个系统，下午用欧陆的涉密笔记本做开发吧。</span></p><p id=\"uf87fe490\" class=\"ne-p\"><br></p><p id=\"u2b00620e\" class=\"ne-p\"><span class=\"ne-text\">顶着雨回家，到家先吃个饭，看两集忍者神龟，从下班回家到吃完饭已经过去两小时了。</span></p></div>";
        String s = richTextToPureText(content);
        System.out.println(s);
        Set<String> wordSet = new HashSet<>();
        List<Term> terms = HanLP.segment(s);
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

    private static String richTextToPureText(String richText) {
        if (StrUtil.isBlank(richText)) return StrUtil.EMPTY;
        // Jsoup解析HTML并提取纯文本（自动处理所有标签和属性）
        Document doc = Jsoup.parse(richText);
        return doc.text().trim();
    }
}

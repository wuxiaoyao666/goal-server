package com.xiaoyao.goal;

import com.xiaoyao.goal.utils.BlindGenerator;

/**
 * @author 逍遥
 */
public class HanlpTest {
    public static void main(String[] args) {
        String text = "1. 和马老师沟通 Group 业务逻辑，沟通过程非常顺畅，开心！\n" +
                "2. 中午打了一把金铲铲，首次 12 玉龙。同时三星索拉卡和三星派克，虽然拿了第二,但是：爽！\n" +
                "3. 之前买了6颗牛油果，生吃一颗太难吃了。今天早上带一颗去公司，下午睡醒后点一杯外卖(用优惠券会便宜些)，然后去店里把牛油果给商家，让他给我加奶茶里，商家都懵逼了，没见过这种奇葩要求哈哈哈哈哈，喝了一口，感觉还可以，就是味道没那么甜了。\n" +
                "4. 第一次吃鹿肉！去武胜老板还多给了一个海带结，开心！";
        String segment = BlindGenerator.segment(text);
        System.out.println(segment);

        String keyword = "奶茶";
        String keywordHash = BlindGenerator.hashToken(keyword);
        System.out.println(segment.contains(keywordHash));
    }
}

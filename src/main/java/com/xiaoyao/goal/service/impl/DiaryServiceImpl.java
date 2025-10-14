package com.xiaoyao.goal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.entity.DiaryKeyword;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryVO;
import com.xiaoyao.goal.mapper.DiaryMapper;
import com.xiaoyao.goal.service.IDiaryKeywordService;
import com.xiaoyao.goal.service.IDiaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 日记表 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
@Slf4j
@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> implements IDiaryService {

    /**
     * 排除的词类型
     * w: 标点符号 f: 方位词 p: 介词 c: 连词 y: 语气词 o: 拟声词 q: 量词
     */
    private final Set<String> StopNatures = Set.of("w", "f", "p", "c", "y", "o", "q");

    private final Set<String> StopWords = new HashSet<>();

    @Autowired
    private IDiaryKeywordService diaryKeywordService;

    /**
     * 初始化停用词
     */
    @PostConstruct
    public void initStopWords() {
        try {
            String lines = ResourceUtil.readUtf8Str("stopwords.txt");
            StopWords.addAll(Arrays.stream(lines.split(System.lineSeparator())).collect(Collectors.toSet()));
            log.info("加载停用词表完成。");
        } catch (Exception e) {
            log.error("加载停用词表失败 {} 。", e.getMessage());
        }
    }

    @Override
    @Transactional
    public DiaryVO sync(SaveDiaryDTO body) {
        Long userId = StpUtil.getLoginIdAsLong();
        Diary diary = Diary.builder()
                .id(body.getId())
                .userId(userId)
                .title(body.getTitle())
                .content(body.getContent())
                .tags(body.getTags()).build();
        // 保存或修改日记
        saveOrUpdate(diary);
        String content = body.getContent();
        if (!StrUtil.isBlank(content)) {
            // JsonP 去除标签
            Document doc = Jsoup.parse(content);
            content = doc.text().trim();
        }
        if (ObjUtil.isNotNull(body.getId())) {
            // 删除旧关键词关联
            diaryKeywordService.remove(Wrappers.<DiaryKeyword>lambdaQuery().eq(DiaryKeyword::getDiaryId, body.getId()));
        }
        // 生成盲索引
        Set<DiaryKeyword> diaryKeywords = new HashSet<>();
        List<Term> terms = HanLP.segment((CollUtil.isNotEmpty(diary.getTags()) ? String.join(StrUtil.SPACE, diary.getTags()) : StrUtil.EMPTY) + StrUtil.SPACE + body.getTitle() + StrUtil.SPACE + content);
        terms.forEach(term -> {
            String word = term.word;
            String nature = term.nature.toString();
            if (StrUtil.isNotBlank(word) && StrUtil.isNotBlank(nature) && !StopWords.contains(word) && !StopNatures.contains(nature)) {
                diaryKeywords.add(DiaryKeyword.builder()
                        .diaryId(diary.getId())
                        .keywordHash(SecureUtil.md5(word))
                        .build());
            }
        });
        if (CollUtil.isNotEmpty(diaryKeywords)) {
            diaryKeywordService.saveBatch(diaryKeywords);
        }
        return DiaryVO.toDiaryVO(getById(diary.getId()));
    }

    @Override
    public IPage<DiaryVO> search(SearchDiaryDTO body) {
        IPage<Diary> diaryPage;
        if (StrUtil.isNotBlank(body.getKeyword())) {
            List<Term> terms = HanLP.segment(body.getKeyword());
            // 提取分词结果
            Set<String> keywordSet = terms.stream()
                    .map(term -> SecureUtil.md5(term.word))
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toSet());
            List<DiaryKeyword> diaryKeywords = diaryKeywordService.list(
                    new LambdaQueryWrapper<DiaryKeyword>()
                            .in(DiaryKeyword::getKeywordHash, keywordSet)
            );
            if (diaryKeywords.isEmpty()) return new Page<>(body.getCurrent(), body.getLimit());
            // 提取命中的 ID
            Set<Long> diaryIds = diaryKeywords.stream().map(DiaryKeyword::getDiaryId).collect(Collectors.toSet());
            diaryPage = page(
                    new Page<>(body.getCurrent(), body.getLimit()),
                    new LambdaQueryWrapper<Diary>()
                            .in(Diary::getId, diaryIds)
                            .eq(Diary::getUserId, StpUtil.getLoginIdAsLong())
                            .orderByDesc(Diary::getCreateTime)
            );
        } else {
            diaryPage = page(
                    new Page<>(body.getCurrent(), body.getLimit()),
                    new LambdaQueryWrapper<Diary>()
                            .eq(Diary::getUserId, StpUtil.getLoginIdAsLong())
                            .orderByDesc(Diary::getCreateTime)
            );
        }
        List<DiaryVO> diaryData = diaryPage.getRecords().stream()
                .map(DiaryVO::toDiaryVO).toList();
        IPage<DiaryVO> result = new Page<>(body.getCurrent(), body.getLimit());
        result.setRecords(diaryData);
        result.setTotal(diaryPage.getTotal());
        return result;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        diaryKeywordService.remove(Wrappers.<DiaryKeyword>lambdaQuery().eq(DiaryKeyword::getDiaryId, id));
        removeById(id);
    }
}

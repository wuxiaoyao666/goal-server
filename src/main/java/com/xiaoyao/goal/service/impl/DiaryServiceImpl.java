package com.xiaoyao.goal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.entity.DiaryKeyword;
import com.xiaoyao.goal.entity.dto.diary.ExportDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SearchDiaryDTO;
import com.xiaoyao.goal.entity.vo.DiaryHotTagsVO;
import com.xiaoyao.goal.entity.vo.DiaryVO;
import com.xiaoyao.goal.exception.GoalException;
import com.xiaoyao.goal.mapper.DiaryKeywordMapper;
import com.xiaoyao.goal.mapper.DiaryMapper;
import com.xiaoyao.goal.service.IDiaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.goal.service.export.ExportHandler;
import com.xiaoyao.goal.service.export.ExportHandlerFactory;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
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

    /**
     * 停用词列表
     */
    private final Set<String> StopWords = new HashSet<>();

    @Autowired
    private DiaryKeywordMapper diaryKeywordMapper;

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
        Diary diary = Diary.builder()
                .id(body.getId())
                .userId(StpUtil.getLoginIdAsLong())
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
            diaryKeywordMapper.delete(Wrappers.<DiaryKeyword>lambdaQuery().eq(DiaryKeyword::getDiaryId, body.getId()));
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
                        .word(word.trim().toLowerCase())
                        .build());
            }
        });
        if (CollUtil.isNotEmpty(diaryKeywords)) {
            System.out.println(JSONUtil.toJsonStr(diaryKeywords));
            diaryKeywordMapper.insert(diaryKeywords);
        }
        return DiaryVO.toDiaryVO(getById(diary.getId()));
    }

    @Override
    public IPage<DiaryVO> search(SearchDiaryDTO body) {
        IPage<Diary> diaryPage;
        Set<Long> diaryIds = null;
        if (StrUtil.isNotBlank(body.getKeyword())) {
            List<Term> terms = HanLP.segment(body.getKeyword());
            // 提取分词结果
            Set<String> keywordSet = terms.stream()
                    .map(term -> term.word)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toSet());
            // 查询索引表
            diaryIds = diaryKeywordMapper.selectDistinctDiaryIdsByWords(keywordSet);
            if (diaryIds.isEmpty()) return new Page<>(body.getCurrent(), body.getLimit());
        }
        diaryPage = page(
                new Page<>(body.getCurrent(), body.getLimit()),
                new LambdaQueryWrapper<Diary>()
                        .eq(Diary::getUserId, StpUtil.getLoginIdAsLong())
                        .in(CollUtil.isNotEmpty(diaryIds), Diary::getId, diaryIds)
                        .ge(ObjUtil.isNotNull(body.getStartTime()), Diary::getCreateTime, body.getStartTime())
                        .le(ObjUtil.isNotNull(body.getEndTime()), Diary::getCreateTime, body.getEndTime())
                        .orderByDesc(Diary::getCreateTime)
        );
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
        diaryKeywordMapper.delete(Wrappers.<DiaryKeyword>lambdaQuery().eq(DiaryKeyword::getDiaryId, id));
        removeById(id);
    }

    @Override
    public List<DiaryHotTagsVO> hotTags(Integer count) {
        return baseMapper.selectHotTags(StpUtil.getLoginIdAsLong(), count);
    }

    @Override
    public void export(ExportDiaryDTO body, HttpServletResponse response) {
        // 查询符合条件的日记
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getUserId, StpUtil.getLoginIdAsLong());
        if (ObjUtil.isNotNull(body.getStartTime())) wrapper.ge(Diary::getCreateTime, body.getStartTime());
        if (ObjUtil.isNotNull(body.getEndTime())) wrapper.le(Diary::getCreateTime, body.getEndTime());
        List<Diary> diaries = list(wrapper);
        if (CollUtil.isEmpty(diaries)) throw new GoalException("未查询到符合条件的日记。");
        // 导出日记
        ExportHandler handler = ExportHandlerFactory.getHandler(body.getType());
        byte[] exportDiaries = handler.export(diaries);
        response.setContentType(handler.getContentType());
        String filename = DateUtil.format(new Date(), "yyyyMMddHHmmss") + body.getType().getValue();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setContentLength(exportDiaries.length);
        // 写入响应流
        try (OutputStream os = response.getOutputStream()) {
            os.write(exportDiaries);
            os.flush();
        } catch (IOException e) {
            throw new GoalException(String.format("导出失败: %s", e.getMessage()));
        }
    }
}

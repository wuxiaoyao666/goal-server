package com.xiaoyao.goal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.goal.entity.dto.diary.ExportDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.diary.SearchDiaryDTO;
import com.xiaoyao.goal.service.IDiaryService;
import com.xiaoyao.goal.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 日记表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
@SaCheckLogin
@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private IDiaryService diaryService;

    @PostMapping("/sync")
    public Result sync(@RequestBody @Validated SaveDiaryDTO body) {
        return Result.success(diaryService.sync(body));
    }

    @PostMapping("/search")
    public Result search(@RequestBody @Validated SearchDiaryDTO body) {
        return Result.success(diaryService.search(body));
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Long id) {
        diaryService.delete(id);
        return Result.success();
    }

    @GetMapping("/hotTags")
    public Result hotTags(@RequestParam(defaultValue = "20") Integer count) {
        return Result.success(diaryService.hotTags(count));
    }

    @PostMapping("export")
    public Result export(@RequestBody ExportDiaryDTO body, HttpServletResponse response) {
        diaryService.export(body, response);
        return Result.success();
    }
}

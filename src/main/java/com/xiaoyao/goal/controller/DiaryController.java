package com.xiaoyao.goal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.service.IDiaryService;
import com.xiaoyao.goal.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <p>
 * 日记表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-07-27
 */
@SaCheckLogin
@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private IDiaryService diaryService;

    @PostMapping("/sync")
    public Result sync(@RequestBody @Validated SaveDiaryDTO body){
        return Result.success(diaryService.sync(body));
    }

    @PostMapping("/info")
    public Result info(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate body) {
        return Result.success(diaryService.info(body));
    }

    @GetMapping("/recordDays")
    public Result recordDays(){
        return Result.success(diaryService.recordDays());
    }

    @PostMapping("/search")
    public Result search(@RequestBody @Validated SearchDiaryDTO body){
        return Result.success(diaryService.search(body));
    }
}

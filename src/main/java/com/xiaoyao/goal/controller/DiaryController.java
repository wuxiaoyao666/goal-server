package com.xiaoyao.goal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.xiaoyao.goal.entity.dto.SaveDiaryDTO;
import com.xiaoyao.goal.entity.dto.SearchDiaryDTO;
import com.xiaoyao.goal.service.IDiaryService;
import com.xiaoyao.goal.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 日记表 前端控制器
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-08
 */
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
}

package com.xiaoyao.goal.service;

import com.xiaoyao.goal.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 图片 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-10-14
 */
public interface IPictureService extends IService<Picture> {

    /**
     * 文件上传
     *
     * @param file 文件
     * @param id   用户 ID
     * @return 文件信息
     */
    PictureVO upload(MultipartFile file, Long id);
}

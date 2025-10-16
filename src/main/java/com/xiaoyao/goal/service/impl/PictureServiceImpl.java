package com.xiaoyao.goal.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.goal.constant.GoalConstant;
import com.xiaoyao.goal.entity.Picture;
import com.xiaoyao.goal.entity.bo.ImageMetadataBO;
import com.xiaoyao.goal.entity.vo.PictureVO;
import com.xiaoyao.goal.exception.GoalException;
import com.xiaoyao.goal.cos.CosManager;
import com.xiaoyao.goal.mapper.PictureMapper;
import com.xiaoyao.goal.service.IPictureService;
import com.xiaoyao.goal.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author 逍遥
 */
@Slf4j
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private CosManager cosManager;

    @Override
    public PictureVO upload(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty())
            throw new GoalException("文件不能为空");
        // 校验文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!GoalConstant.AllowUploadImageList.contains(suffix))
            throw new GoalException("不支持的文件类型");
        File tempFile = null;
        String hash = null;
        try {
            tempFile = File.createTempFile("upload_", ".tmp");
            file.transferTo(tempFile);
            // 生成文件 hash 码
            hash = DigestUtil.md5Hex(tempFile);
            // 校验图片是否上传过
            Picture findByHash = getOne(Wrappers.<Picture>lambdaQuery().eq(Picture::getHash, hash).eq(Picture::getUserId, userId));
            if (findByHash != null) {
                return new PictureVO(findByHash.getUrl(), findByHash.getPicSize(), findByHash.getName());
            }
            // 用 hash 生成文件名，防止并发上传同一张图片
            String uploadFileName = String.format("%s.%s", hash, suffix);
            String uploadPath = String.format("%s/%s", userId, uploadFileName);
            // 上传图片
            String url = cosManager.upload(uploadPath, tempFile);
            // 入库
            ImageMetadataBO imageMetadata = ImageUtils.getImageMetadata(tempFile);
            Picture picture = new Picture();
            BeanUtils.copyProperties(imageMetadata, picture);
            picture.setUrl(url);
            picture.setName(uploadFileName);
            picture.setHash(hash);
            picture.setUserId(userId);
            save(picture);
            return new PictureVO(url, imageMetadata.getPicSize(), uploadFileName);
        } catch (DuplicateKeyException e) {
            // 并发触发唯一约束，再次查询图片
            Picture existPic = lambdaQuery()
                    .eq(Picture::getHash, hash)
                    .eq(Picture::getUserId, userId)
                    .one();
            if (existPic != null) {
                return new PictureVO(existPic.getUrl(), existPic.getPicSize(), existPic.getName());
            }
            throw new GoalException("服务异常，请重试.");
        } catch (Exception e) {
            throw new GoalException(e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                boolean deleteResult = tempFile.delete();
                if (!deleteResult) {
                    log.error("临时文件删除异常,路径:{}", tempFile.getAbsolutePath());
                }
            }
        }
    }
}

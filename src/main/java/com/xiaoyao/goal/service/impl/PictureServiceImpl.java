package com.xiaoyao.goal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * @author 逍遥
 */
@Slf4j
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private CosManager cosManager;

    @Override
    public PictureVO upload(MultipartFile file, Long id) {
        if (file == null || file.isEmpty())
            throw new GoalException("文件不能为空");
        // 1. 校验文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!GoalConstant.AllowUploadImageList.contains(suffix))
            throw new GoalException("不支持的文件类型");
        // 2. 生成新的图片文件名和目录
        String uuid = RandomUtil.randomString(16);
        String uploadFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, suffix);
        String uploadPath = String.format("%s/%s", id, uploadFileName);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(uploadPath, null);
            file.transferTo(tempFile);
            // 3. 校验图片是否上传过
            String hash = DigestUtil.md5Hex(tempFile);
            Picture findByHash = getOne(Wrappers.<Picture>lambdaQuery().eq(Picture::getHash, hash));
            if (findByHash != null) {
                return new PictureVO(findByHash.getUrl(), findByHash.getPicSize(), findByHash.getName());
            }
            // 4. 上传图片
            String url = cosManager.upload(uploadPath, tempFile);
            // 5. 入库
            ImageMetadataBO imageMetadata = ImageUtils.getImageMetadata(tempFile);
            Picture picture = new Picture();
            BeanUtils.copyProperties(imageMetadata, picture);
            picture.setUrl(url);
            picture.setName(uploadFileName);
            picture.setHash(hash);
            picture.setUserId(id);
            save(picture);
            return new PictureVO(url, imageMetadata.getPicSize(), uploadFileName);
        } catch (Exception e) {
            throw new GoalException(e.getMessage());
        } finally {
            if (tempFile != null) {
                boolean deleteResult = tempFile.delete();
                if (!deleteResult) {
                    log.error("临时文件删除异常,路径:{}", tempFile.getAbsolutePath());
                }
            }
        }
    }
}

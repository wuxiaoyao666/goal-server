package com.xiaoyao.goal.utils;

import com.xiaoyao.goal.entity.bo.ImageMetadataBO;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author 逍遥
 */
public class ImageUtils {
    /**
     * 获取图片元信息
     *
     * @param imageFile 待解析的图片文件
     * @return 解析后的图片信息对象
     * @throws IllegalArgumentException 文件不存在或非有效文件时抛出
     * @throws IOException              读取图片失败时抛出（如非图片文件、损坏等）
     */
    public static ImageMetadataBO getImageMetadata(File imageFile) throws IOException {
        // 校验文件有效性
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IllegalArgumentException("文件不存在或不是有效文件: " + imageFile.getPath());
        }

        // 初始化结果对象
        ImageMetadataBO result = new ImageMetadataBO();

        // 提取文件基础信息
        result.setPicSize(imageFile.length());            // 文件大小（字节）

        // 读取图片元数据（格式、宽高）
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile)) {
            // 获取支持该图片格式的 ImageReader
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
            if (!readers.hasNext()) {
                throw new IOException("不支持的图片格式，无法解析");
            }

            // 使用第一个可用的 ImageReader 读取图片
            ImageReader reader = readers.next();
            reader.setInput(imageInputStream);

            // 获取图片格式
            String formatName = reader.getFormatName().toLowerCase(); // 统一小写
            result.setPicFormat(formatName);

            // 获取图片宽高
            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            result.setPicWidth(width);
            result.setPicHeight(height);

            // 计算宽高比（宽/高，保留小数）
            double scale = height == 0 ? 0.0 : (double) width / height;
            result.setPicScale(scale);
        } catch (IOException e) {
            throw new IOException("解析图片失败，可能是非图片文件或文件损坏", e);
        }
        return result;
    }
}

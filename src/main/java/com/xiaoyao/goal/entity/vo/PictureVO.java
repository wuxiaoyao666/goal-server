package com.xiaoyao.goal.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 逍遥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureVO {
    private String url;
    private long size;
    @JsonProperty("filename")
    private String fileName;
}
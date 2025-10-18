package com.xiaoyao.goal.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author 逍遥
 */
@Data
public class UpdateUserDTO {
    /**
     * 用户名
     */
    @NotBlank
    @Size(min = 3, max = 20, message = "用户名长度3-20位")
    private String username;

    /**
     * 昵称
     */
    @NotBlank
    private String nickname;

    /**
     * 头像
     */
    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            message = "头像链接格式不正确（需为http/https/ftp开头的有效URL）"
    )
    private String avatar;

    /**
     * 性别：1:男 2:女
     */
    @Range(min = 1,max = 2,message = "性别只能为 1 or 2")
    private Byte sex;
}

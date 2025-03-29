package com.xiaoyao.flow.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author 逍遥
 */
@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 10, message = "用户名长度在1-10位之间")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20位之间")
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String email;
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式错误")
    private String phone;
    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 20, message = "昵称长度在1-20位之间")
    private String nickname;
    @NotBlank(message = "头像不能为空")
    private String avatar;
    private Byte sex = 1;
}

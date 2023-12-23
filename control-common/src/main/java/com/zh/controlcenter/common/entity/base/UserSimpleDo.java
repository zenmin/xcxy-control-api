package com.zh.controlcenter.common.entity.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Describle This Class Is redis存储用户信息
 * @Author ZengMin
 * @Date 2020/9/11 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "令牌")
    private String token;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "ip", hidden = true)
    private String ip;

    public UserSimpleDo(Long id, String ip) {
        this.id = id;
        this.ip = ip;
    }
}

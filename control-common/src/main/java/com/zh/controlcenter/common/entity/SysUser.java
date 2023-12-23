package com.zh.controlcenter.common.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.zh.controlcenter.common.entity.base.AbstractEntityModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 系統用户
 * JPA只用来正向生成数据库表和字段 如果不需要此字段更新 请加上注解@TableField(exist = false)和@Transient
 *
 * @Author
 * @Date 2023-04-26 14:34:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系統用户")
@Table(name = "sys_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends AbstractEntityModel {

    @Schema(description = "设备唯一编码")
    private String deviceId;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "姓名 2~10位")
    @Excel(name = "姓名", width = 20)
    private String nickName;

    @Schema(description = "手机号")
    @Excel(name = "手机号", width = 30)
    private String phone;

    @Schema(description = "我的星愿 限制16字")
    @Excel(name = "我的星愿", width = 40)
    private String title;

    @Schema(description = "星愿故事 限制120字")
    @Excel(name = "星愿故事", width = 50)
    @Column(columnDefinition = "varchar(1000)")
    private String content;

    @Schema(description = "年龄 0~100")
    @Excel(name = "年龄")
    private Integer age;

    @Schema(description = "用户状态1启用 0禁用 ")
    private Integer status;

    @Schema(description = "地标建筑id  前端定义")
    private String buildingId;

    @Schema(description = "是否领取优惠券")
    @Column(columnDefinition = "bit(1) default 0")
    private Boolean isGet;

    @Schema(description = "许愿币个数")
    @Column(columnDefinition = "int(11)  default 0")
    private Integer coins;

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "地标建筑名称  前端定义")
    private String buildingName;

}

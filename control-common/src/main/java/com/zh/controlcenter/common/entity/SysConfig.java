package com.zh.controlcenter.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zh.controlcenter.common.entity.base.AbstractEntityModel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * JPA只用来正向生成数据库表和字段 如果不需要此字段更新 请加上注解@TableField(exist = false)和@Transient
 *
 * @Author
 * @Date 2023-12-22 10:18:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@Schema(description = "系统配置")
@Table(name = "sys_config")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends AbstractEntityModel {

    @Schema(description = "当前参与人数")
    private String personCount;

    @Schema(description = "首页海报底图URL", hidden = true)
    private String posterImg;

    @Schema(description = "大奖信息")
    private String prize;

    @Schema(description = "活动规则")
    private String rule;

    @Schema(description = "个人海报底图", hidden = true)
    private String personImg;

    @Schema(description = "活动状态 状态1进行中 0未开始 2已结束 ")
    private Integer status;

    @Schema(description = "每多少分钟加的人数  例如：100 表示每5分钟加100~100x2的人数", hidden = true)
    private Integer personCountAdd;

    @Schema(description = "背景音乐")
    private String bgm;


}

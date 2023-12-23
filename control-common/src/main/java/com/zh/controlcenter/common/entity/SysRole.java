package com.zh.controlcenter.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zh.controlcenter.common.entity.base.AbstractEntityModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统角色
 * JPA只用来正向生成数据库表和字段 如果不需要此字段更新 请加上注解@TableField(exist = false)和@Transient
 *
 * @Author 
 * @Date 2023-04-27 15:30:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(description = "系统角色")
@Table(name = "sys_role")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SysRole extends AbstractEntityModel {

    @Schema(description = "入口页面")
    private String mainPage;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色描述")
    private String remark;

    @Schema(description = "状态 1启用 0禁用")
    private Integer status;

    @Schema(description = "是否系统预设,系统预设不允许删除")
    private Boolean isSystem;


}

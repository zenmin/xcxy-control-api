package com.zh.controlcenter.common.entity.base;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Describle This Class Is 实体Model
 * @Author ZengMin
 * @Date 2019/3/14 16:36
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@MappedSuperclass
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class AbstractEntityModel implements Serializable {

    @Id
    @Column(unique = true, columnDefinition = "bigint COMMENT '主键'")
    @Schema(description = "主键")
    private Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP COMMENT '创建时间' ")
    @Schema(description = "创建时间", hidden = true)
    @Excel(name = "领取时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", columnDefinition = "datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ")
    @Schema(description = "更新时间", hidden = true)
    private Date updateTime;

}


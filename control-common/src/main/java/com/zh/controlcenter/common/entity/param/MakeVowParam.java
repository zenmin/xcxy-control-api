package com.zh.controlcenter.common.entity.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Describle
 * @Author ZengMin
 * @Date 2023/12/22 0022 10:47
 */
@Data
@Schema(name = "许愿参数")
public class MakeVowParam {

    @Schema(description = "设备id , 有就传 , 判断是否是同一个用户")
    private String deviceId;

    @Excel(name = "*姓名 2~10字")
    private String nickName;

    @Schema(description = "年龄 0~100", required = true)
    private Integer age;

    @Schema(description = "手机号", required = true)
    private String phone;

    @Schema(description = "我的星愿  限制16字", required = true)
    private String title;

    @Schema(description = "星愿故事  限制120字", required = true)
    private String content;

    @Schema(description = "地标建筑id  前端定义", required = true)
    private String buildingId;

    @Schema(description = "地标建筑名称  前端定义", required = true)
    private String buildingName;

}

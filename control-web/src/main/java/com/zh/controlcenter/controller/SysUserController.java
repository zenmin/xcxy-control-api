package com.zh.controlcenter.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.zh.controlcenter.common.apibase.ResponseEntity;
import com.zh.controlcenter.common.constant.CommonConstant;
import com.zh.controlcenter.common.constant.RateLimiter;
import com.zh.controlcenter.common.entity.SysConfig;
import com.zh.controlcenter.common.entity.SysUser;
import com.zh.controlcenter.common.entity.param.MakeVowParam;
import com.zh.controlcenter.common.util.CommonUtil;
import com.zh.controlcenter.common.util.IpHelper;
import com.zh.controlcenter.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.zh.controlcenter.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系統用户
 *
 * @Author
 * @Date 2023-04-27 11:40:51
 */
@Tag(name = "系統用户")
@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysConfigService configService;

    private SysConfig getConfig() {
        SysConfig one = configService.getOne(new QueryWrapper<SysConfig>().last("limit 1"));
        return one;
    }

    @PostMapping("/getConfig")
    @Operation(summary = "页面一：1、查询系统配置")
    public ResponseEntity<SysConfig> getSysConfig() {
        return ResponseEntity.success(this.getConfig());
    }

    @PostMapping("/getLightenBuildingIds")
    @Operation(summary = "页面二：1、查询点亮的建筑", description = "返回点亮的地标建筑ID")
    @Parameter(name = "deviceId", description = "设备唯一编码", required = true)
    public ResponseEntity<List<String>> getBuildingIds(@RequestBody @Parameter(hidden = true) JSONObject params) {
        String deviceId = params.getString("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            return ResponseEntity.success(Lists.newArrayList());
        }
        List<SysUser> list = sysUserService.list(new QueryWrapper<SysUser>().eq("phone", params.getString("phone")).or().eq("device_id", deviceId).select("building_id").groupBy("building_id"));
        return ResponseEntity.success(list.stream().map(SysUser::getBuildingId).collect(Collectors.toList()));
    }

    @PostMapping("/makeVow")
    @Operation(summary = "页面二：2、许愿", description = "许愿成功会返回一个deviceId。前端需要缓存到本地localStorage里面，用来页面二查询点亮的地标建筑的")
//    @RateLimiter(value = "2", target = CommonConstant.LIMIT_USER_IP)
    public ResponseEntity<List<Map>> makeVow(@RequestBody MakeVowParam makeVowParam, ServerWebExchange exchange) {
        String requestIpAddr = IpHelper.getIpAddress(exchange.getRequest());
        SysConfig config = this.getConfig();
        Integer status = config.getStatus();
        if (Objects.equals(status, CommonConstant.STATUS_ERROR)) {
            return ResponseEntity.error("活动已结束~");
        }
        if (Objects.equals(status, CommonConstant.STATUS_VALID)) {
            return ResponseEntity.error("活动未开始~");
        }
        if (StringUtils.isBlank(makeVowParam.getNickName()) || StringUtils.isBlank(makeVowParam.getPhone())
                || Objects.isNull(makeVowParam.getAge()) || StringUtils.isBlank(makeVowParam.getTitle()) || StringUtils.isBlank(makeVowParam.getContent())) {
            return ResponseEntity.error("请完整填写哦~");
        }
        if (makeVowParam.getNickName().length() < 2 || makeVowParam.getNickName().length() > 10) {
            return ResponseEntity.error("姓名需要在2~8个汉字之间哦~");
        }
        if (makeVowParam.getAge() <= 0 || makeVowParam.getAge() > 100) {
            return ResponseEntity.error("年龄填写有误哦~");
        }
        CommonUtil.validPhone(makeVowParam.getPhone());

        // 查询手机号
        SysUser existsUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", makeVowParam.getPhone()).last("limit 1"));
        if (Objects.nonNull(existsUser)) {
            return ResponseEntity.error(999, "您已经许过愿了哦~", ImmutableMap.of("deviceId", existsUser.getDeviceId()));
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(makeVowParam, sysUser);
        if (StringUtils.isBlank(sysUser.getDeviceId())) {
            sysUser.setDeviceId(CommonUtil.UUID());
        }
        sysUser.setStatus(CommonConstant.STATUS_OK);
        sysUser.setCoins(1);
        sysUser.setIp(requestIpAddr);
        sysUserService.save(sysUser);
        return ResponseEntity.success(sysUser);
    }

    @PostMapping("/getCoins")
    @Operation(summary = "页面三：1、查询许愿币个数")
    @Parameter(name = "phone", description = "手机号", required = true)
    public ResponseEntity<Integer> getCoins(@RequestBody @Parameter(hidden = true) JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isBlank(phone)) {
            return ResponseEntity.success(0);
        }
        CommonUtil.validPhone(phone);
        SysUser one = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", params.getString("phone")).select("id,coins"));
        return ResponseEntity.success(one.getCoins());
    }

    @PostMapping("/putin")
    @Operation(summary = "页面三：2、投入许愿池")
    @Parameter(name = "phone", description = "手机号", required = true)
    //    @RateLimiter(value = "2", target = CommonConstant.LIMIT_USER_IP)
    public ResponseEntity<Boolean> putin(@RequestBody @Parameter(hidden = true) JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isBlank(phone)) {
            return ResponseEntity.error("许愿币不足，快去点亮建筑许愿吧~");
        }
        CommonUtil.validPhone(phone);
        SysUser one = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", params.getString("phone")).select("id,coins"));
        if (one.getCoins() > 0) {
            one.setCoins(one.getCoins() - 1);
            sysUserService.updateById(one);
            return ResponseEntity.success(true);
        }
        return ResponseEntity.error("许愿币不足，快去点亮建筑许愿吧~");
    }


    @PostMapping("/getCoupon")
    @Operation(summary = "页面四：1、领取优惠券")
    @Parameter(name = "phone", description = "手机号", required = true)
    @RateLimiter(value = "2", target = CommonConstant.LIMIT_USER_IP)
    public ResponseEntity<Boolean> getCoupon(@RequestBody @Parameter(hidden = true) JSONObject params) {
        String phone = params.getString("phone");
        if (StringUtils.isBlank(phone)) {
            return ResponseEntity.success(0);
        }
        CommonUtil.validPhone(phone);
        SysUser one = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", params.getString("phone")).select("id", "coins", "is_get"));
        if (Objects.isNull(one)) {
            return ResponseEntity.error("你还没有许愿哦，快去点亮建筑许愿吧~");
        }
        if (one.getCoins() >= 1) {
            one.setCoins(one.getCoins() - 1);
        }
        if (one.getIsGet()) {
            return ResponseEntity.success(true);
        }
        one.setIsGet(true);
        boolean b = sysUserService.updateById(one);
        return ResponseEntity.success(b);
    }


    @GetMapping("/getQrImg")
    @Operation(summary = "页面四：2、生成宣传海报")
    @Parameter(name = "phone", description = "手机号", required = true)
    @RateLimiter(value = "2", target = CommonConstant.LIMIT_USER_IP)
    public Mono<org.springframework.http.ResponseEntity<byte[]>> getQrImg(@RequestParam(value = "phone", required = true) String phone) {
        SysConfig config = this.getConfig();
        if (config.getStatus() == CommonConstant.STATUS_ERROR) {
            return this.writeError("活动还没有开始哦，再等等吧~");
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(config.getPersonImg());
        } catch (Exception e) {
            ClassPathResource classPathResource = new ClassPathResource("/static/phb.png");
            try {
                inputStream = classPathResource.getInputStream();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", phone).eq("status", CommonConstant.STATUS_OK));
        if (Objects.isNull(user)) {
            return this.writeError("你还没有许愿哦，快去点亮建筑许愿吧~");
        }
        if (user.getCoins() >= 1) {
            return this.writeError("你还没有投入许愿池哦，清先把许愿币投入许愿池吧~");
        }
        // 处理图片 加上姓名 我的星愿 星愿故事
        BufferedImage image = ImgUtil.read(inputStream);
        Graphics2D g2d = image.createGraphics();
        // 设置字体和颜色
        Font font = new Font("Microsoft YaHei Light", Font.BOLD, 26);
        g2d.setFont(font);
        g2d.setColor(new Color(66, 66, 66));
        g2d.drawString("@" + user.getNickName(), 260, 450);

        Font font1 = new Font("Microsoft YaHei Light", Font.BOLD, 16);
        g2d.setFont(font1);
        int x = 220;
        int y = 493;
        g2d.setColor(new Color(93, 93, 93));
        g2d.drawString(user.getTitle().length() >= 16 ? (user.getTitle().substring(0, 16) + "...") : user.getTitle(), x, y);
        int split = user.getContent().length() / 16;
        y += 30;
        for (int i = 0; i <= split; i++) {
            int z = (i > 0 ? 20 : 0);
            if (i == split) {
                g2d.drawString(user.getContent().substring(i * 16), x, y += z);
            } else {
                g2d.drawString(user.getContent().substring(i * 16, (i + 1) * 16), x, y += z);
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImgUtil.write(image, "PNG", outputStream);

        return Mono.just(org.springframework.http.ResponseEntity.ok().headers(o -> {
            o.setContentType(MediaType.IMAGE_PNG);
        }).body(outputStream.toByteArray()));
    }

    public Mono<org.springframework.http.ResponseEntity<byte[]>> writeError(String errorMsg) {
        return Mono.just(org.springframework.http.ResponseEntity.ok().headers(o -> {
            o.set("Content-Type", "application/json;encoding=UTF-8");
        }).body(JSONObject.toJSONString(ResponseEntity.error(errorMsg)).getBytes(Charset.defaultCharset())));
    }

    @GetMapping("/export")
    @Operation(summary = "导出已领优惠券的用户")
    public Mono<org.springframework.http.ResponseEntity<byte[]>> export() {
        List<SysUser> list = sysUserService.list(new QueryWrapper<SysUser>().eq("is_get", true).eq("status", CommonConstant.STATUS_OK).orderByDesc("create_time"));
        ExportParams exportParams = new ExportParams("优惠券领取用户导出 " + DateUtil.now(), "Sheet1", ExcelType.XSSF);
        exportParams.setCreateHeadRows(true);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, SysUser.class, list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(org.springframework.http.ResponseEntity.ok().headers(o -> {
            o.set("Content-Type", "application/vnd.ms-excel;encoding=UTF-8");
            o.set("Content-Disposition", "attachment;filename=" + URLEncoder.encode(exportParams.getTitle() + "." + ExcelTypeEnum.XLSX.getValue(), Charset.defaultCharset()));
        }).body(baos.toByteArray()));
    }


    /**
     * 定时加人数
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void addUser() {
        SysConfig config = this.getConfig();
        if (config.getStatus() != CommonConstant.STATUS_OK) {
            return;
        }
        Integer personCountAdd = config.getPersonCountAdd();
        if (Objects.isNull(personCountAdd)) {
            return;
        }
        // 计算
        StringBuilder c = new StringBuilder(String.valueOf(Integer.parseInt(Optional.ofNullable(config.getPersonCount()).orElse("0")) + RandomUtil.randomInt(personCountAdd, personCountAdd * 2)));
        while (c.length() < 6) {
            c.insert(0, "0");
        }
        configService.update(null, new UpdateWrapper<SysConfig>().eq("id", config.getId()).set("person_count", c.toString()));
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"), XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
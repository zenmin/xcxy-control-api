package com.zh.controlcenter.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Describle mybatis plus generator
 * @Author ZengMin
 * @Date
 */
public class CodeGenerator {
    /**
     * 一些基础配置
     * 注意表中字段不要带下划线 每个字段尽量加上COMMENT
     */
    // 要生成的表名 可以为多个
    static final String[] tableName = {"sys_config"};
    // author
    static final String author = "";
    // 全包名
    static final String packageName = "com.zh.controlcenter";
    // 数据库url
    static String jdbcUrl;
    // 数据库用户名
    static String username;
    // 数据库密码
    static String password;
    // 开启swagger
    static final boolean isSwagger = true;

    static String projectPath = null;

    static {
        projectPath = System.getProperty("user.dir");
        Dict dict = YamlUtil.loadByPath(projectPath + "/control-web/src/main/resources/config/application-dev.yml");
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(dict)).getJSONObject("spring").getJSONObject("datasource");
        jdbcUrl = object.get("url").toString();
        username = object.get("username").toString();
        password = object.get("password").toString();
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setServiceName("%sService"); // 默认加了I 这里去掉
        gc.setSwagger2(isSwagger);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(jdbcUrl);
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(scanner("模块名"));
        pc.setParent(packageName);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // 可以自定义写注入模板的值
                Map<String, Object> map = new HashMap<>();
                map.put("packageName", packageName);
                this.setMap(map);
            }
        };

        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("/ftl/entity.java");
        templateConfig.setService("/ftl/service.java");
        templateConfig.setServiceImpl("/ftl/serviceImpl.java");
        templateConfig.setController("/ftl/controller.java");
        templateConfig.setMapper("/ftl/mapper.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);   // 表名驼峰
        strategy.setColumnNaming(NamingStrategy.no_change);// 驼峰关闭
        strategy.setSuperEntityClass("com.zh.controlcenter.common.entity.base.AbstractEntityModel");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableName);
        strategy.setSuperEntityColumns("id", "createTime", "create_time", "update_time");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
        String packageName = "/src/main/java/com/zh/controlcenter";
        String src = projectPath + packageName;
        FileUtil.move(new File(src + "/controller"), new File(projectPath + "/control-web" + packageName), true);
        FileUtil.move(new File(src + "/entity"), new File(projectPath + "/control-common" + packageName + "/common"), true);
        FileUtil.move(new File(src + "/service"), new File(projectPath + "/control-service" + packageName), true);
        FileUtil.move(new File(src + "/mapper"), new File(projectPath + "/control-dao" + packageName + "/dao"), true);
        FileUtil.del(new File(projectPath + "/src"));
    }

}


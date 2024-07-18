package com.zh.controlcenter.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.FileReader;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.google.common.collect.Lists;
import com.zh.controlcenter.common.util.CommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class MybatisPlusConfig {
    /**
     * 新增分页拦截器，并设置数据库类型为mysql
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    public static void main(String[] args) throws FileNotFoundException {
        StringBuffer sb = new StringBuffer("[");
        List<String> strings = FileUtil.readLines(new File("E:\\LLM\\CareGPT-main\\data\\ChatMed_Consult-v0.3.json"), Charset.defaultCharset());
        strings.forEach(line -> {
            sb.append(line).append(",\n");
        });
        sb.append("]");
        FileUtil.writeString(sb.toString(), new File("D:\\Desktop\\ChatMed_Consult-v0.3.json"), Charset.forName("UTF-8"));
    }

}

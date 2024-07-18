package com.zh.controlcenter.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kuanghuan
 */
@Component
@Slf4j
public class FontConfig implements CommandLineRunner {

    @Override
    public void run(String... args) {
        File ttfFile = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("font/msyhl.ttc");
            InputStream inputStream = classPathResource.getInputStream();
            ttfFile = File.createTempFile("/tmp/msyhl.ttc", ".ttc");
            try {
                FileUtils.copyInputStreamToFile(inputStream, ttfFile);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, ttfFile);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            log.info("load font:" + font.getFontName());
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        } finally {
            if (ttfFile != null) {
                FileUtils.deleteQuietly(ttfFile);
            }
        }
    }

}
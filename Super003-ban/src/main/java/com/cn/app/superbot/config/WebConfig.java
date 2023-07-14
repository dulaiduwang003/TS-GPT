package com.cn.app.superbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    /**
     * The File save path.
     */
    @Value("${file.path}")
    private String fileSavePath;

    /**
     * Add resource handlers.
     *
     * @param registry the registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:"+fileSavePath);
    }


}

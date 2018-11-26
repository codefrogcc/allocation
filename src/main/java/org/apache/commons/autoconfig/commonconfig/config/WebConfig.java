package org.apache.commons.autoconfig.commonconfig.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/admin").setViewName("adminLogin");
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                super.configureMessageConverters(converters);
                FastJsonHttpMessageConverter fastConverter=new FastJsonHttpMessageConverter();
                FastJsonConfig fastJsonConfig=new FastJsonConfig();
                fastJsonConfig.setSerializerFeatures(
                        SerializerFeature.PrettyFormat
                );
                fastConverter.setFastJsonConfig(fastJsonConfig);
                converters.add(fastConverter);
            }
        };
        return webMvcConfigurerAdapter;
    }
}

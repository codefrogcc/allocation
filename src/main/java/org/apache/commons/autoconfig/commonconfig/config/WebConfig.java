package org.apache.commons.autoconfig.commonconfig.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.access.AccessInterceptor;
import org.apache.commons.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    AccessInterceptor accessInterceptor;

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/admin").setViewName("adminLogin");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {

                registry.addInterceptor(loginInterceptor).addPathPatterns("/main/**")
                        .excludePathPatterns("/main/subaccount/adminLogin","/main/subaccount/login"); //.excludePathPatterns("api/path/login");

                registry.addInterceptor(accessInterceptor);
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                super.configureMessageConverters(converters);
                FastJsonHttpMessageConverter fastConverter=new FastJsonHttpMessageConverter();
                FastJsonConfig fastJsonConfig=new FastJsonConfig();
                fastJsonConfig.setSerializerFeatures(
                        SerializerFeature.PrettyFormat
                );
                List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
                fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
                fastConverter.setSupportedMediaTypes(fastMediaTypes);
                fastConverter.setFastJsonConfig(fastJsonConfig);
                converters.add(fastConverter);
            }
        };
        return webMvcConfigurerAdapter;
    }
}

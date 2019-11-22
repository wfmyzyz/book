package com.wfmyzyz.book.config;

import com.wfmyzyz.book.interceptor.AdminLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Bean
    public AdminLoginInterceptor adminLoginInterceptor(){
        return new AdminLoginInterceptor();
    }

    /**
     * 允许跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOrigins("*").allowedMethods("*");

    }

    /**
     * 打开拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor()).addPathPatterns("/back/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/outimg/**").addResourceLocations("file:E:/ideawork/IDEA/book/");
    }

}

package com.bfdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 初始化拦截器
 */
@SpringBootConfiguration
//@EnableWebMvc
public class InitializeInterceptor implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    /***
     * 这个方法是用来配置静态资源的，比如html，js，css，等等
     * @param registry
     *  继承WebMvcConfigurationSupport类会导致自动配置失效
     * 所以这里要指定默认的静态资源的位置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }


    /**
     *  //注册拦截器
     * 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
     *         // 静态资源： *.css , *.js
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 静态资源： *.css , *.js
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login","/logout")
                .excludePathPatterns("/configuration/initConfigurationWhetherPath","/configuration/testConnectionJDBC","/configuration/initConfiguration")
                .excludePathPatterns("/","/error","/asserts/**","/webjars/**","/Content/**","/lib/**");
    }

}

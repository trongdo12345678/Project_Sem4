package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.interceptor.CartInterceptor;
import com.interceptor.HeaderInterceptor;

import com.interceptor.AuthInterceptor;



@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private HeaderInterceptor headerInterceptor;
    @Autowired
    private CartInterceptor cartInterceptor;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		
		
		  registry.addInterceptor(new AuthInterceptor()) .addPathPatterns("/admin/**",
		  "/warehouseManager/**" ,"/businessManager/**", "/role/**")
		  .excludePathPatterns("login", "/access-denied");
		  
		  registry.addInterceptor(headerInterceptor) .addPathPatterns("/",
		  "/shoppingpage/**", "/detailproduct/**", "/account/**", "/cart/**",
		  "/order/**", "/checkout/**", "/contact/**");
		  registry.addInterceptor(cartInterceptor) .addPathPatterns("/cart/**");
		 

      
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
    }

}


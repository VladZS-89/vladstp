package com.example.vladstp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

//класс, который содержит конфигурацию вэб-слоя и странички, на которых описаны шаблоны без логики.
//нам нужен только login

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}") //7й добавляем загрузку файлов
    private String uploadPath;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override //7й
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**") //7й каждое обращение к серверу по пути img  будет перенаправлять ...
                .addResourceLocations("file:///" + uploadPath + "/"); //7й ... по пути
        registry.addResourceHandler("/static/**") //7й чтобы раздавать статич рес-сы
                .addResourceLocations("classPath:/static/"); //7й в дереве проекта ищет ресурсы по указанному пути
    }

    //настройки шаблонизатора FreeMarker
    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setSuffix(".ftl");
        return resolver;
    }
}
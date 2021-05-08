package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private IngredientRepository JpaIngredientRepo;
    @Autowired
    public WebConfig(IngredientRepository JpaIngredientRepo){
        this.JpaIngredientRepo=JpaIngredientRepo;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new IngredientByIdConverter(JpaIngredientRepo));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/guessing").setViewName("guessing");

    }

}

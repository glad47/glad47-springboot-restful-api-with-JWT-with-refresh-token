package com.example.demo.Repository.Config;

import com.example.demo.Service.IngredientByIdConverter;
import com.example.demo.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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

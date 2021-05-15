package com.example.demo;

import lombok.Data;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;


import javax.persistence.Id;
@Data
public class IngredientResource extends RepresentationModel<IngredientResource> {


    private final String name;
    private final Ingredient.Type type;
    public IngredientResource(Ingredient ingredient){
        this.name=ingredient.getName();
        this.type=ingredient.getType();
    }
}

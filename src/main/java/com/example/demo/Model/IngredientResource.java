package com.example.demo.Model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class IngredientResource extends RepresentationModel<IngredientResource> {


    private final String name;
    private final Ingredient.Type type;
    public IngredientResource(Ingredient ingredient){
        this.name=ingredient.getName();
        this.type=ingredient.getType();
    }
}

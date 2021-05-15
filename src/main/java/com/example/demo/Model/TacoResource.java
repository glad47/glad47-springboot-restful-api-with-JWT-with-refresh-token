package com.example.demo.Model;

import com.example.demo.Service.IngredientResourceAssembler;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
public class TacoResource extends RepresentationModel<TacoResource> {

    private final Date createdAt;
    private final String name;
    private final CollectionModel<IngredientResource> ingredients;
    public TacoResource(Taco taco){
        this.createdAt=taco.getCreatedAt();
        this.name=taco.getName();
        this.ingredients=new IngredientResourceAssembler()
                .toCollectionModel(taco.getIngredients());
    }


}

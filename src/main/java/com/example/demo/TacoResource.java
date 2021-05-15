package com.example.demo;

import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
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

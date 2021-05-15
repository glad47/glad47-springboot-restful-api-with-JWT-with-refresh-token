package com.example.demo.Service;

import com.example.demo.Controller.IngredientController;
import com.example.demo.Model.Ingredient;
import com.example.demo.Model.IngredientResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
public class IngredientResourceAssembler implements RepresentationModelAssembler<Ingredient, IngredientResource> {


    @Override
    public IngredientResource toModel(Ingredient ingredient) {
        IngredientResource ingredientResource = new IngredientResource(ingredient);

        ingredientResource.add(linkTo(
                methodOn(IngredientController.class)
                        .getIngredientById(ingredient.getId()))
                .withSelfRel());

        return ingredientResource;
    }

    @Override
    public CollectionModel<IngredientResource> toCollectionModel(Iterable<? extends Ingredient> entities) {
        CollectionModel<IngredientResource> ingredientResources = RepresentationModelAssembler.super.toCollectionModel(entities);
        return ingredientResources;

    }
}

package com.example.demo.Service;


import com.example.demo.Controller.DDesignController;
import com.example.demo.Model.Taco;
import com.example.demo.Model.TacoResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
@Service
public class TacoResourceAssembler implements RepresentationModelAssembler<Taco, TacoResource> {

//    @Override
//    protected TacoResource instantiateResource(Taco taco){
//        return new TacoResource(taco);
//    }
//    @Override
//    public TacoResource toResource(Taco taco){
//        return createResourceWithId(taco.getId(),taco);
//    }

    @Override
    public TacoResource toModel(Taco taco) {
        TacoResource tacoResource = new TacoResource(taco);

        tacoResource.add(WebMvcLinkBuilder.linkTo(
                methodOn(DDesignController.class)
                        .tacoById(taco.getId()))
                .withSelfRel());


        return tacoResource;
    }

    @Override
    public CollectionModel<TacoResource> toCollectionModel(Iterable<? extends Taco> entities) {
        CollectionModel<TacoResource> tacoResources= RepresentationModelAssembler.super.toCollectionModel(entities);

        return tacoResources;
    }
}

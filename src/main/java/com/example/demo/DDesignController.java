package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path="/design1",produces="application/json")
@CrossOrigin(origins = "*")
public class DDesignController {

        private IngredientRepository jpaIngredientRepo;
        private TacoRepository jpaTacoRepo;
        @Autowired
        public DDesignController(IngredientRepository jpaIngredientRepo,
                                    TacoRepository jpaTacoRepo){
            this.jpaIngredientRepo=  jpaIngredientRepo;
            this.jpaTacoRepo=jpaTacoRepo;
        }



    @GetMapping(path = "/{id}",consumes = "application/json")
    public ResponseEntity<?> tacoById(@PathVariable("id") long id){
        Optional<Taco> opTaco=this.jpaTacoRepo.findById(id);
        if(opTaco.isPresent()){
            return new ResponseEntity<>(opTaco.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/recent1")
    public ResponseEntity<?> recentTacos(){
        PageRequest page=PageRequest.of(0,12, Sort.by("createdAt").descending());
        Optional<List<Taco>> opListTaco= Optional.of(this.jpaTacoRepo.findAll(page).getContent());
        if(opListTaco.isPresent()){
            CollectionModel<TacoResource> recentsResources=
                    new TacoResourceAssembler().toCollectionModel(opListTaco.get());
//            CollectionModel<TacoResource> recentsResources=
//                    new CollectionModel<TacoResource>(tacoResources);

            recentsResources.add(
                    linkTo(methodOn(DDesignController.class).recentTacos())
                            .withRel("recents")
            );

            return new ResponseEntity<>(recentsResources,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }



    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> processDesign(@RequestBody Taco taco){

             jpaTacoRepo.save(taco);
          return ResponseEntity.ok(taco);

    }

    @PutMapping(path="/{id}",consumes="application/json")
    public ResponseEntity<?> patchTaco(@RequestBody Taco patch,
                                        @PathVariable("id")long id){
        Optional<Taco> opTaco=this.jpaTacoRepo.findById(id);
        if(opTaco.isPresent()){
            Taco taco=opTaco.get();
            if(patch.getName() != null){
                taco.setName(patch.getName());
            }
            if(patch.getIngredients() != null){
                taco.setIngredients(patch.getIngredients());
            }

            this.jpaTacoRepo.save(taco);
            return new ResponseEntity<>(taco,HttpStatus.OK);

        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("{/id}")
    public ResponseEntity<?> deleteTaco(@PathVariable("id")long id) {
        Optional<Taco> opTaco = this.jpaTacoRepo.findById(id);
        if (opTaco.isPresent()) {
            try {

                this.jpaTacoRepo.deleteById(id);
                return new ResponseEntity<>(opTaco.get(), HttpStatus.OK);
            } catch (EmptyResultDataAccessException e) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            }
        }else{
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        }
    }

}

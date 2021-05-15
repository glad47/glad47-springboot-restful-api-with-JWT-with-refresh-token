package com.example.demo.Controller;


import com.example.demo.Model.Ingredient;
import com.example.demo.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="ingredients",produces = "application/json")
@CrossOrigin(origins = "*")
public class IngredientController {
    private IngredientRepository ingredientRepo;
    @Autowired
    public IngredientController(IngredientRepository ingredientRepo){
       this.ingredientRepo=ingredientRepo;
    }

    @GetMapping("/all")
    public Iterable<Ingredient> getIngredients(){
        return ingredientRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable String id){
        Optional<Ingredient> ingredientOpt=this.ingredientRepo.findById(id);
        if(ingredientOpt.isPresent()){
            return new ResponseEntity<>(ingredientOpt.get(), HttpStatus.OK);

        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }
}

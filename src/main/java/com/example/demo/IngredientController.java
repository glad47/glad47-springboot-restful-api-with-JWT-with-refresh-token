package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
@CrossOrigin(origins = "*")
public class IngredientController {
    private IngredientRepository ingredientRepo;
    @Autowired
    public IngredientController(IngredientRepository ingredientRepo){
       this.ingredientRepo=ingredientRepo;
    }

    @GetMapping("/ingredients")
    public Iterable<Ingredient> getIngredients(){
        return ingredientRepo.findAll();
    }
}

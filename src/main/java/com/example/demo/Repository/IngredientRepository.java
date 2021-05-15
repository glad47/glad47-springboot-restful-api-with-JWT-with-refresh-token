package com.example.demo.Repository;

import com.example.demo.Model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,String> {

}

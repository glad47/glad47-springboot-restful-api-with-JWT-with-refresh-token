package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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




    @GetMapping("/recent1")
    public ResponseEntity<?> recentTacos(){
        PageRequest page=PageRequest.of(0,12, Sort.by("createdAt").descending());
        return  ResponseEntity.ok(this.jpaTacoRepo.findAll(page).getContent());
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> processDesign(@RequestBody Taco taco){

             jpaTacoRepo.save(taco);
          return ResponseEntity.ok(taco);

    }

}

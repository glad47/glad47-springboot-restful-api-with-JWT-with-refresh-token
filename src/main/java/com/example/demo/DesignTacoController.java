package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.Ingredient.Type;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/design")
//@CrossOrigin(origins = "*")
@SessionAttributes("order")
public class DesignTacoController {
    private IngredientRepository jpaIngredientRepo;
    private TacoRepository jpaTacoRepo;
    @Autowired
    public DesignTacoController(IngredientRepository jpaIngredientRepo,
        TacoRepository jpaTacoRepo){
        this.jpaIngredientRepo=  jpaIngredientRepo;
        this.jpaTacoRepo=jpaTacoRepo;
    }
    @ModelAttribute(name="order")
    public Order order(){
        return new Order();
    }
    @ModelAttribute(name="taco")
    public Taco taco(){
        return new Taco();
    }
    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients=new ArrayList<>();
        jpaIngredientRepo.findAll().forEach(i->ingredients.add(i));
        Type[] types=Ingredient.Type.values();
        for(Type type : types){
            model.addAttribute(type.toString().toLowerCase(),filterByType(ingredients,type));
        }
//        model.addAttribute("design", new Taco());
        return "design";
    }
    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {

        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());

    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute Taco taco, Errors errors,
                                @ModelAttribute Order order) {

    if(errors.hasErrors()){
        return "design";
    }
    // Save the taco design...
    // We'll do this in chapter 3
        Taco saved=jpaTacoRepo.save(taco);
        order.addDesign(saved);
        log.info("Processing design: " + taco);
        return "redirect:/orders/current";
    }


}

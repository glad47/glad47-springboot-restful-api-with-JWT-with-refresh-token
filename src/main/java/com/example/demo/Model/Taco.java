package com.example.demo.Model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor

@Entity
public class Taco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdAt;
    private String name;
    @ManyToMany(targetEntity = Ingredient.class)
    private List<Ingredient> ingredients;
    @PrePersist
    void createdAt(){
        this.createdAt=new Date();
    }



}
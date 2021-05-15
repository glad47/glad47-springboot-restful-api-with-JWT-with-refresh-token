package com.example.demo.Model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE,force=true)
@Entity
public class Ingredient {
    @NonNull
    @Id
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final Type type;

 

    public static enum Type{
        WRAP,PROTEIN,VEGGIES,CHEESE,SAUCE
    }
}

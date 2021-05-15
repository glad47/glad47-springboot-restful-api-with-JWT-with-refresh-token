package com.example.demo.Model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
//import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor

@Entity
@Table(name="Taco_Order")
public class Order {
    private static final long serialValueUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date placedAt;




    private String deliveryName;


    private String deliveryStreet;


    private String deliveryCity;


    private String deliveryState;

    private String deliveryZip;


    private String ccNumber;



    private String ccExpiration;


    private String ccCVV;
    @ManyToMany(targetEntity = Taco.class)
    private List<Taco> tacos = new ArrayList<>();

    @ManyToOne
    private User user;

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }
    @PrePersist
    void placedAt(){
        this.placedAt=new Date();
    }


}
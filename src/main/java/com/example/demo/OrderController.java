package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path="/orders",produces="application/json")
@CrossOrigin(origins = "*")
public class OrderController {
    private OrderRepository jpaOrderRepos;
    @Autowired
    public OrderController(OrderRepository jdbcOrderRepos){
        this.jpaOrderRepos=jdbcOrderRepos;
    }

//    @GetMapping("/current")
//    public String orderForm(@RequestBody Model model){
////        model.addAttribute("order",new Order());
//
//        return "OrderForm";
//    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> processOrder(@RequestBody  Order order,
                                          @AuthenticationPrincipal User user) {


        order.setUser(user);
        jpaOrderRepos.save(order);
        return ResponseEntity.ok(order);

    }

}

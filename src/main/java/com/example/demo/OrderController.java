package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
    private OrderRepository jpaOrderRepos;
    @Autowired
    public OrderController(OrderRepository jdbcOrderRepos){
        this.jpaOrderRepos=jdbcOrderRepos;
    }

    @GetMapping("/current")
    public String orderForm(Model model){
//        model.addAttribute("order",new Order());
        return "OrderForm";
    }

    @PostMapping
    public String processOrder(@Valid  @ModelAttribute Order order, Errors errors,
                               SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user) {
       if(errors.hasErrors()){
           return "OrderForm";
       }

        order.setUser(user);
        jpaOrderRepos.save(order);
        sessionStatus.setComplete();
        log.info("Order submitted: " + order);
        return "redirect:/";
    }

}

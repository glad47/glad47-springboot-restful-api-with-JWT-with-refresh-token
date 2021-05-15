package com.example.demo.Controller;


import com.example.demo.Model.Order;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.support.SessionStatus;
//
//import javax.validation.Valid;
import java.util.Optional;

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
    public ResponseEntity<?> processOrder(@RequestBody Order order,
                                          @AuthenticationPrincipal User user) {


        order.setUser(user);
        jpaOrderRepos.save(order);
        return ResponseEntity.ok(order);

    }

    @PutMapping(path="/{orderId}",consumes="application/json")
    public ResponseEntity<?> patchOrder(@RequestBody Order patch,
                                        @PathVariable("orderId")long orderId){
        Optional<Order> opOrder=this.jpaOrderRepos.findById(orderId);
        if(opOrder.isPresent()){
            Order order=opOrder.get();
            if(patch.getDeliveryName() != null){
           order.setDeliveryName(patch.getDeliveryName());
            }
            if(patch.getDeliveryStreet() != null){
                order.setDeliveryStreet(patch.getDeliveryStreet());
            }
            if(patch.getDeliveryCity() != null){
                order.setDeliveryCity(patch.getDeliveryCity());
            }
            if(patch.getDeliveryState() != null){
                order.setDeliveryState(patch.getDeliveryState());
            }
            if(patch.getDeliveryZip() != null){
                order.setDeliveryZip(patch.getDeliveryZip());
            }
            if(patch.getCcNumber() != null){
                order.setCcNumber(patch.getCcNumber());
            }
            if(patch.getCcExpiration() != null){
                order.setCcExpiration(patch.getCcExpiration());
            }
            if(patch.getCcCVV() != null){
                order.setCcCVV(patch.getCcCVV());
            }
            this.jpaOrderRepos.save(order);
            return new ResponseEntity<>(order,HttpStatus.OK);

        }else{
         return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("{/id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id")long id) {
        Optional<Order> opOrder = this.jpaOrderRepos.findById(id);
        if (opOrder.isPresent()) {
            try {

                this.jpaOrderRepos.deleteById(id);
                return new ResponseEntity<>(opOrder.get(), HttpStatus.OK);
            } catch (EmptyResultDataAccessException e) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            }
        }else{
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        }
    }
    @GetMapping(path = "/{id}",consumes = "application/json")
    public ResponseEntity<?> orderById(@PathVariable("id") long id){
        Optional<Order> opOrder=this.jpaOrderRepos.findById(id);
        if(opOrder.isPresent()){
            return new ResponseEntity<>(opOrder.get(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

}

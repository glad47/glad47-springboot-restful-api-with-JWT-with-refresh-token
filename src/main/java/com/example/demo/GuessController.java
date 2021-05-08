package com.example.demo;

import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class GuessController {
    private final int rand=(int )(Math.random() *100)+1;
    @GetMapping("/guess")
    public String guessing(@RequestParam("num") String num, Model model){
        int no=Integer.valueOf(num);
        if(no > rand){
            model.addAttribute("guessing","the number you entered is high");
        }
        else if(no < rand){
            model.addAttribute("guessing","the number you entered is less");

        }else{
            model.addAttribute("guessing","the number you entered correct");

        }

        return "GUESSING";

    }
}

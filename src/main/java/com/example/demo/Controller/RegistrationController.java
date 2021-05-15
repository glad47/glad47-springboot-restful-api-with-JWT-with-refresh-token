package com.example.demo.Controller;

import com.example.demo.Model.RegistrationForm;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register",produces = "application/json")
@CrossOrigin(origins = "*")
public class RegistrationController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }
//    @GetMapping
//    public String registerForm(){
//        return "Registration";
//    }
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> processRegistration(@RequestBody RegistrationForm form){
        userRepository.save(form.toUser(passwordEncoder));
        System.out.println("registered successfully");
        return ResponseEntity.ok(form);
    }
}

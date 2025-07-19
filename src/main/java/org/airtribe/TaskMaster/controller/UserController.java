package org.airtribe.TaskMaster.controller;

import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;
import org.airtribe.TaskMaster.dto.UserDTO;
import org.airtribe.TaskMaster.entity.User;
import org.airtribe.TaskMaster.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;

 @GetMapping("/tokenHello")
  public String tokenHello() {
    return "Hello from auth";
  }
    

    @PostMapping("/register")
    public User register(@RequestBody UserDTO user) {
        User registeredUser = authenticationService.registerUser(user);
        String generatedToken = UUID.randomUUID().toString();
        String applicationUrl = "http://localhost:9200/verifyRegistration?token=" + generatedToken;
        System.out.println("Verification link: " + applicationUrl);
        authenticationService.registerVerificationToken(registeredUser, generatedToken);
        return registeredUser;
    }

    @PostMapping("/verifyRegistration")
    public String verifyToken(@RequestParam String token) {
        boolean isTokenValid = authenticationService.validateRegisteredToken(token);

        if(isTokenValid){
            authenticationService.enableUser(token);
            return "token validation successful , enable user, please login to proceed";
        }else{
            return "token validation failed";
        }   
    }

    @PostMapping("/signin")
    public String login(@RequestParam String userName, @RequestParam String password){
        return authenticationService.loginUser(userName, password);
    }
    
}

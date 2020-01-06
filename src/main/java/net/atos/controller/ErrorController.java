package net.atos.controller;


import net.atos.service.LoginService;
import net.atos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorController {


    private UserService userService;
    private LoginService loginService;

    @Autowired
    public ErrorController(UserService userService,  LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }



}

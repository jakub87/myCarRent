package net.atos.controller;


import net.atos.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String getMain(Model model, Authentication auth)
    {
        model.addAttribute("logged_email",loginService.getLoginFromCredentials(auth));
        model.addAttribute("isAdmin",loginService.isAdmin(auth));
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                         Model model){
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        return "login";
    }





}

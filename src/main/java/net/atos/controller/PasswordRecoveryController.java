package net.atos.controller;


import net.atos.service.AutoMailingService;
import net.atos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PasswordRecoveryController {

    private AutoMailingService autoMailingService;
    private UserService userService;

    private String verificationCode = "";
    private String userEmail = "";
    private boolean checkingCode = true;

    @Autowired
    public PasswordRecoveryController(AutoMailingService autoMailingService, UserService userService) {
        this.autoMailingService = autoMailingService;
        this.userService = userService;
    }

    private String generateVerificationCode(int size)
    {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder(size);

        for (int i = 0 ; i < size; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            stringBuilder.append(alphaNumericString.charAt(index));
        }

        return stringBuilder.toString();
    }


    @GetMapping("/recoverypasswordstep1")
    public String getFormRecoverPassword()
    {
        return "recoveryPassword";
    }


    @GetMapping("/recoverypasswordstep2")
    public String recoverPassword(@RequestParam(value = "email", defaultValue = "") String email,
                                  Model model) {
        Pattern pattern = Pattern.compile(".+\\@.+\\..+");
        Matcher matcher = pattern.matcher(email);
        String message = "";
        if (!matcher.matches()) {
            model.addAttribute("error","Invalid email format!");
            return "recoveryPassword";
        }

        userEmail = email;
        verificationCode = generateVerificationCode(6);

        if (userService.accountIsActive(email)) {
            message = "Your recovery verification code is " + verificationCode;
        }
        else {
            message = "Your account is disabled. Please contact with administrator.";
        }

        autoMailingService.sendMessage(email,message);
        model.addAttribute("error","");
        return "recoveryPasswordCode";
    }

    @GetMapping("/recoverypasswordstep3")
    public String checkingVerificationCode(@RequestParam (value = "userVerificationCode", defaultValue = "") String userVerificationCode,
                                           Model model)
    {
        if (verificationCode.isEmpty() || userEmail.isEmpty()) {
            return "redirect:/login";
        }

        if (!verificationCode.equals(userVerificationCode)) {
            if (checkingCode == false) {
                model.addAttribute("error","Your code is locked! You need to request for a new one.");
                verificationCode = "";
                return "recoveryPasswordCode";
            }

            model.addAttribute("error", "Invalid verification code! Remain last attempt!");
            checkingCode = false;
            return "recoveryPasswordCode";
        }

        model.addAttribute("userEmail",userEmail);
        model.addAttribute("result","");
        return "recoveryPasswordNewPassword";
    }


    @GetMapping("/recoverypasswordstep4")
    public String settingNewPassword(@RequestParam (value = "password", defaultValue = "") String password,
                                     @RequestParam (value = "passworconfirmation", defaultValue = "") String passwordConfirmation,
                                     Model model)
    {
       if (verificationCode.isEmpty() || userEmail.isEmpty()) {
           return "redirect:/login";
       }

        model.addAttribute("userEmail",userEmail);

        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(passwordConfirmation);

        if (!matcher.matches()) {
            model.addAttribute("result","Password is not sufficient strong!");
            return "recoveryPasswordNewPassword";
        }
        else {
            if (password.equals(passwordConfirmation)) {
                userService.changePassword(userEmail, password);
                model.addAttribute("result", "Password has been successfully changed.");
                verificationCode = "";
                return "recoveryPasswordNewPassword";
            } else {
                model.addAttribute("result","Passwords are not equals!");
                return "recoveryPasswordNewPassword";
            }
        }
    }

}

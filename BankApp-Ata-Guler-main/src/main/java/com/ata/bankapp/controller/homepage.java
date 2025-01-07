package com.ata.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class homepage {


    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/service")
    public String service() {
        return "service";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "login";
    }

    @GetMapping("/varliklar")
    public String varliklar() {
        return "varlıklar";
    }

    @GetMapping("/team")
    public String takım() {return "team";}
}
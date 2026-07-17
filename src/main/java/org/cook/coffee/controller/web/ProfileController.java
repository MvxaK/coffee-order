package org.cook.coffee.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String profile(){
        return "profile";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id){
        return "order-details";
    }

}

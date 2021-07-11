package com.MantraBazaar.Mantra_Bazaar;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping("/allProducts")
    public String AllProducts() {
        return "All";
    }
}

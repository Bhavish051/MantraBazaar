package com.MantraBazaar.Mantra_Bazaar;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping("/")
    public String Home() {
        return "Home";
    }
}

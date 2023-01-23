package com.nighthawk.spring_portfolio.mvc.frqs.lightboard1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/light1")
public class LightApiController1 {
    @GetMapping("/getlights")
    public static ResponseEntity<String> getLights () {
        LightBoard1 lb = new LightBoard1(6, 6);
        return new ResponseEntity<>(lb.toString(), HttpStatus.OK);
    }
    
    @GetMapping("/getlights/{color}")
    public static ResponseEntity<String> getLights (@PathVariable("color") String color) {
        LightBoard1 lb = new LightBoard1(6, 6, color);
        return new ResponseEntity<>(lb.toString(), HttpStatus.OK);
    }
}

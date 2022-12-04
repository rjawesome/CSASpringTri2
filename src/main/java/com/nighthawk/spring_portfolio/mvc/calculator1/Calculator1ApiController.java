package com.nighthawk.spring_portfolio.mvc.calculator1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculator1")
public class Calculator1ApiController {
  @GetMapping("/calculate")
  public static ResponseEntity<Double> calculate (@RequestParam("expression") String expression) {
    Calculator1 calculator = new Calculator1(expression);
    System.out.println(expression);
    return new ResponseEntity<Double>(calculator.getResult(), HttpStatus.OK);
  }
}

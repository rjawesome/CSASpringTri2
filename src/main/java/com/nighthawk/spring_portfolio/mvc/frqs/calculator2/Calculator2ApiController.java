package com.nighthawk.spring_portfolio.mvc.frqs.calculator2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator2")
public class Calculator2ApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    
    @PostMapping("/")
    public ResponseEntity<Double> calculate(@RequestBody String expression) throws BadParenthesisException {
        System.out.println(expression);
        Calculator2 calculator = new Calculator2(expression);
        return new ResponseEntity<Double>(calculator.getResult(), HttpStatus.OK);
    }

    @ExceptionHandler({BadParenthesisException.class})
    public ResponseEntity<Object> handleBadUserInput () {
      return new ResponseEntity<>("incomplete parenthesis pairs", HttpStatus.BAD_REQUEST); 
    }
}

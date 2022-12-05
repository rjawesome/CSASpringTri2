package com.nighthawk.spring_portfolio.mvc.calculator2;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/calculator2")
public class Calculator2ApiController {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    
    @GetMapping("/")
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

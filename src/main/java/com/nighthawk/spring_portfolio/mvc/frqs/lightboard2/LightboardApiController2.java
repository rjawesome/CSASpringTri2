package com.nighthawk.spring_portfolio.mvc.frqs.lightboard2;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
@RequestMapping("/api/lightboard2")
public class LightboardApiController2 {
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    
    @GetMapping("/")
    public ResponseEntity<String> getLightBoard(@RequestParam int numRows, @RequestParam int numCols) throws ParseException  {
      LightBoard lightboard = new LightBoard(numRows, numCols);
      // JSONParser parser = new JSONParser();
      // JSONObject json = (JSONObject) parser.parse(lightboard.toString());
      return ResponseEntity.ok(lightboard.toString());
    }
}

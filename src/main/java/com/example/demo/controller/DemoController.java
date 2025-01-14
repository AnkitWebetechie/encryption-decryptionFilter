package com.example.demo.controller;

import com.example.demo.RequestDto;
import com.example.demo.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoController {

    @PostMapping("demo")
    public ResponseEntity<?> processData(@RequestBody RequestDto data, HttpServletResponse response) throws Exception {

        Employee em=new Employee();
        if(data.getId().equals("1")){
            em.setId("1");
            em.setAddress("gurugram");
            em.setName("ankit");
            em.setEmail("email@gmail.com");
            em.setMobile("1234567890");

            ObjectMapper objectMapper =new ObjectMapper();
            String responseString =objectMapper.writeValueAsString(em);
            return new ResponseEntity<>(responseString, HttpStatus.OK);}
        else
            return new ResponseEntity<>("registerd first", HttpStatus.OK);
    }
}

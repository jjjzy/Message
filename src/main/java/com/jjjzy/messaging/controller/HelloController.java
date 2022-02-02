package com.jjjzy.messaging.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello Alice!" + name + "123123";
    }

    @GetMapping("/hi")
    public String hi(@RequestParam(name = "qwer") String name, @RequestParam int age) {
        int i = 0;
        return "Hello " + name + " you are " + age + "years old";
    }

    @GetMapping("/superHello/{name}")
    public String superHello(@PathVariable String name) {
        return "Hello !" + name;
    }
}
//./mvnw spring-boot:run


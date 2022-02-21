package com.netcracker.controller;

import com.netcracker.domain.Customer;
import com.netcracker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/rest")
public class CustomerController {

    @Autowired
    CustomerService customerService;

//    @PostMapping("/customers")
//    @RolesAllowed("user")
//    public <S extends Customer> ResponseEntity<S> save (@RequestBody S entity) {
//        return ResponseEntity.ok(customerService.save(entity));
//    }

    public ResponseEntity<String> registerCustomer(@RequestParam(value = "firstName") String firstName,
                                                   @RequestParam(value = "lastName") String lastName,
                                                   @RequestParam(value = "email") String email,
                                                   @RequestParam(value = "password") String password,
                                                   @RequestParam(value = "confirmPassword") String confirmPassword) {

    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

}

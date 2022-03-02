package com.netcracker.controller;

import com.netcracker.domain.Customer;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


@RestController
@RequestMapping("/rest/users")
public class CustomerController {

    @Autowired
    CustomerService customerService;


    @PostMapping("/createUser")
    public ResponseEntity<String> registerCustomer(@RequestParam(value = "firstName") String firstName,
                                                   @RequestParam(value = "lastName") String lastName,
                                                   @Email @RequestParam(value = "email") String email,
                                                   @RequestParam(value = "password") String password) throws IOException {
        return ResponseEntity.ok(customerService.register(firstName, lastName, email, password));
    }

    @GetMapping("")
    public ResponseEntity<List<Customer>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Customer>> getAllUserById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(customerService.getUserById(id));
    }

    @GetMapping("/accessToken")
    public ResponseEntity<String> getAccessToken() {
        return ResponseEntity.ok(customerService.getAccessToken());
    }

    @PostMapping("/updateUserInfo")
    public ResponseEntity<String> updateUserInfo(@RequestParam(value = "customerId") Long customerId,
                                                 @RequestParam(value = "firstName") String firstName,
                                                 @RequestParam(value = "lastName") String lastName,
                                                 @RequestParam(value = "gender") Gender gender,
                                                 @RequestParam(value = "phoneNumber") String phoneNumber,
                                                 @RequestParam(value = "lastDeliveryAddress") String lastDeliveryAddress,
                                                 @RequestParam(value = "birthDate") String birthDate) {
        return ResponseEntity.ok(customerService.updateCustomerInfo(customerId, firstName, lastName, gender.toString(), phoneNumber, lastDeliveryAddress, birthDate));
    }

    @GetMapping("/authorize")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> authorizeUser() {
        return ResponseEntity.ok("User is authorized");
    }

}

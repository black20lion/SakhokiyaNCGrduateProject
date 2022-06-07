package com.netcracker.controller;

import com.netcracker.domain.Customer;
import com.netcracker.domain.CustomerInfo;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.service.CustomerService;
import com.netcracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest/users")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    TokenService tokenService;

    @RolesAllowed("user")
    @PostMapping("/createUser")
    public ResponseEntity<String> registerCustomer() throws IOException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        return ResponseEntity.ok(customerService.register(tokenService.getGivenNameFromToken(token), tokenService.getFamilyNameFromToken(token), tokenService.getEmailFromToken(token)));
    }

    @RolesAllowed("admin")
    @GetMapping("")
    public ResponseEntity<List<Customer>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAll());
    }


    @RolesAllowed("admin")
    @GetMapping("/{id}")
    public ResponseEntity<List<Customer>> getUserById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(customerService.getUserById(id));
    }

    @RolesAllowed("user")
    @GetMapping("/email")
    public ResponseEntity<CustomerInfo> getCustomerInfo() {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        return ResponseEntity.ok(customerService.getUserInfoByEmail(tokenService.getEmailFromToken(token)));
    }

    @RolesAllowed("user")
    @PostMapping("/updateUserInfo")
    public ResponseEntity<String> updateUserInfo(@RequestParam(value = "firstName") String firstName,
                                                 @RequestParam(value = "lastName") String lastName,
                                                 @RequestParam(value = "gender") Gender gender,
                                                 @RequestParam(value = "phoneNumber") String phoneNumber,
                                                 @RequestParam(value = "lastDeliveryAddress") String lastDeliveryAddress,
                                                 @RequestParam(value = "birthDate") String birthDate) {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        return ResponseEntity.ok(customerService.updateCustomerInfo(id, firstName, lastName, gender.toString(), phoneNumber, lastDeliveryAddress, birthDate));
    }

}

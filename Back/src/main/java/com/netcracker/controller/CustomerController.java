package com.netcracker.controller;

import com.netcracker.domain.Customer;
import com.netcracker.domain.CustomerInfo;
import com.netcracker.domain.enumeration.Gender;
import com.netcracker.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest/users")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RolesAllowed("user")
    @PostMapping("/createUser")
    public ResponseEntity<String> registerCustomer(@RequestParam(value = "firstName") String firstName,
                                                   @RequestParam(value = "lastName") String lastName,
                                                   @RequestParam(value = "email") String email) throws IOException {
        return ResponseEntity.ok(customerService.register(firstName, lastName, email));
    }

    @GetMapping("")
    @RolesAllowed("admin")
    public ResponseEntity<List<Customer>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Customer>> getAllUserById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(customerService.getUserById(id));
    }

    @RolesAllowed("user")
    @GetMapping("/email")
    public ResponseEntity<CustomerInfo> getCustomerInfoByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(customerService.getUserInfoByEmail(email));
    }

    @RolesAllowed("user")
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


}

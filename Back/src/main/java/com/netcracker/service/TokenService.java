package com.netcracker.service;
import com.netcracker.repository.CustomerRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Service
public class TokenService {

    @Autowired
    CustomerRepository customerRepository;

    public Long getIdByEmail(String email) {
        return customerRepository.getCustomerIdByEmail(email).get(0);
    }

    public Long getIdFromToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String tokenRdyToUse = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(tokenRdyToUse.toString());
        String email = (String) jsonObject.get("email");
        Long id = getIdByEmail(email);
        return id;
    }

    public String getEmailFromToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String tokenRdyToUse = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(tokenRdyToUse.toString());
        String email = (String) jsonObject.get("email");
        return email;
    }

    public String getGivenNameFromToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String tokenRdyToUse = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(tokenRdyToUse.toString());
        String givenName = (String) jsonObject.get("given_name");
        return givenName;
    }

    public String getFamilyNameFromToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String tokenRdyToUse = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(tokenRdyToUse.toString());
        String familyName = (String) jsonObject.get("family_name");
        return familyName;
    }
}
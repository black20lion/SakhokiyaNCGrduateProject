package com.netcracker.service;

import com.netcracker.domain.Customer;
import com.netcracker.repository.CustomerInfoRepository;
import com.netcracker.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.dao.*;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String localParameter = "lkadsjfAffasGSGJYKI43d9f2jfASADSDFf232oFSGkjnfAWF23sg2sgrssgsjifqQsgEsgsfsdRsT2sg324";

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public List<Customer> getUserById(Long id) {
        return customerRepository.findAllById(id);
    }

    public String getAccessToken() {
        try {
            String urlParameters = "grant_type=client_credentials&client_id=admin-cli&client_secret=shvAiGsNcfUVsAIpV39HhGKcPWwf8GB1";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "http://localhost:8080/realms/master/protocol/openid-connect/token";
            URL url = new URL(request);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            content.delete(984, 1093);
            content.delete(0, 17);
            return content.toString();
        } catch (IOException exception) {
            return exception.toString();
        }
    }

    public String registerBase(String email, String password) {
        String salt = generateSalt().toString();
        String hashedRow = localParameter + salt + password;
        String hash = bCryptPasswordEncoder.encode(hashedRow);
        customerRepository.createCustomer(email, hash, salt);
        return "User is successfully added";
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[8];
        random.nextBytes(bytes);
        return bytes;
    }

    public String registerKeyCloak(String firstName, String lastName, String email, String password) throws IOException {
        String urlParameters = "{\"firstName\":\"" + firstName + "\", \"lastName\":\"" + lastName + "\", \"email\":\"" + email + "\", \"enabled\":\"true\"}";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = "http://localhost:8080/admin/realms/shop/users";
        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        con.setUseCaches(false);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        setUserPassword(password, getUserKeycloakIdByUsername(email));
        setUserRole(getUserKeycloakIdByUsername(email));

        return "User is successfully added";
    }

    public String register(String firstName, String lastName, String email, String password) {
        try {
            registerKeyCloak(firstName, lastName, email, password);
        } catch (IOException exception) {
            if (exception.toString().contains("409")) {
                return "User with this email already exists";
            } else return "Registration error";
        }

        try {
            registerBase(email, password);
            customerInfoRepository.registerCustomerInfo(customerRepository.getUserId().get(0), firstName, lastName);
        } catch (Throwable throwable) {
            deleteUserById(getUserKeycloakIdByUsername(email));
            if (throwable instanceof DataIntegrityViolationException) {
                if (throwable.getMessage().contains("unique_email")) {
                    return "User with this email already exists";
                } else return "database error";
            } else{
                throwable.printStackTrace();return "unknown error";}
        }
        return "User is successfully added";
    }

    public String getUserKeycloakIdByUsername(String email) {
        try {
            String accessToken = getAccessToken();
            String request = "http://localhost:8080/admin/realms/shop/users?username=" + email;
            URL url = new URL(request);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setInstanceFollowRedirects(false);

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            String result = content.substring(8, 44);
            return result;
        } catch (
                IOException exception) {
            return exception.toString();
        }
    }

    public String setUserPassword(String password, String id) {
        try {
            String urlParameters = "{\"type\":\"" + password + "\", \"temporary\":\"false\", \"value\":\"" + password + "\"}";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String accessToken = getAccessToken();
            String request = "http://localhost:8080/admin/realms/shop/users/" + id + "/reset-password";
            URL url = new URL(request);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(false);

            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setRequestProperty("charset", "utf-8");

            con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();

        } catch (
                IOException exception) {
            return exception.toString();
        }
    }

    public String deleteUserById(String id) {
        try {
            String accessToken = getAccessToken();
            String request = "http://localhost:8080/admin/realms/shop/users/" + id;
            URL url = new URL(request);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setInstanceFollowRedirects(false);

            con.setRequestMethod("DELETE");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        } catch (
                IOException exception) {
            return exception.toString();
        }
    }

    public String setUserRole(String id) {
        try {
            String urlParameters = "[{\"id\":\"01721490-e61f-4a3a-a3eb-9fc2a52790e6\",\"name\":\"user\"}]";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String accessToken = getAccessToken();

            String request = "http://localhost:8080/admin/realms/shop/users/" + id + "/role-mappings/realm";
            URL url = new URL(request);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(false);

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setRequestProperty("charset", "utf-8");

            con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        } catch (
                IOException exception) {
            return exception.toString();
        }
    }

    public String updateCustomerInfo(Long customerId, String firstName, String lastName, String gender, String phoneNumber, String lastDeliveryAddress, String birthDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Moscow"));
        boolean error = false;
        java.util.Date utilDate = null;
        try {
            utilDate = formatter.parse(birthDate);
        } catch (ParseException exception) {
            error = true;
        }


        if (birthDate == null || error || utilDate == null) {
            customerInfoRepository.updateCustomerInfoDateNull(customerId, firstName, lastName, gender, phoneNumber, lastDeliveryAddress);
        } else {
            Date parsedBirthDate = new Date(utilDate.getTime());
            customerInfoRepository.updateCustomerInfo(customerId, firstName, lastName, gender, phoneNumber, lastDeliveryAddress, parsedBirthDate);
        }
        return "Information is successfully updated";
    }
}


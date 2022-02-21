package com.netcracker.service;

import com.netcracker.domain.Customer;
import com.netcracker.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository repository;

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public <S extends Customer> S save(S entity) {
        return repository.save(entity);
    }

    public String getRegistrationToken() {
        try {
            String urlParameters  = "grant_type=client_credentials&client_id=admin-cli&client_secret=shvAiGsNcfUVsAIpV39HhGKcPWwf8GB1";
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "http://localhost:8180/realms/master/protocol/openid-connect/token";
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

            try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write( postData );
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

    public String registerKeyCloak(String firstName, String lastName, String email, String password, String confirmPassword, Long Id) throws IOException {
        String urlParameters = "{\"firstName\":\"" + firstName + "\", \"lastName\":\"" + lastName + "\", \"email\":\"" + email + "\", \"enabled\":\"true\" }";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = "http://localhost:8180/admin/realms/shop/users";
        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + getRegistrationToken());
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        con.setUseCaches(false);
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write( postData );
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

        return "User is successfully added";
    }

}

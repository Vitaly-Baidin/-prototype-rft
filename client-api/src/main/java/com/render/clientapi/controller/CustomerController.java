package com.render.clientapi.controller;

import com.render.clientapi.dto.request.CustomerRequest;
import com.render.clientapi.dto.response.CustomerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "${renderf.server.uri.customer}", produces = "application/json")
public class CustomerController {

    @Value("${renderf.server.url}")
    private String serverUrl;

    @Value("${renderf.server.uri.customer}")
    private String customerUri;

    private final RestTemplate restTemplate;

    public CustomerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request) {
        return restTemplate.postForEntity(serverUrl + customerUri, request, CustomerResponse.class);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        return restTemplate.getForEntity(serverUrl + customerUri, CustomerResponse[].class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        return restTemplate.getForEntity(serverUrl + customerUri + "/" + id, CustomerResponse.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest request) {
        return restTemplate.postForEntity(serverUrl + customerUri + "/" + id, request, CustomerResponse.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        restTemplate.delete(serverUrl + customerUri + "/" + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

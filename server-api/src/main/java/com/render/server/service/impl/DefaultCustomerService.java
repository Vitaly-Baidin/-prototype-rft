package com.render.server.service.impl;

import com.render.server.domain.Customer;
import com.render.server.dto.request.CustomerRequest;
import com.render.server.dto.response.CustomerResponse;
import com.render.server.repository.CustomerRepository;
import com.render.server.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository repository;

    public DefaultCustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> createCustomer(CustomerRequest request) {

        Customer customer = new Customer();

        try {
            checkCustomerName(request.getName());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        customer.setName(request.getName());
        customer.setPassword(request.getPassword());

        repository.save(customer);

        return new ResponseEntity<>(
                createCustomerResponse(customer),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerResponse> customerResponseList = createCustomerResponseList(repository.findAll());
        return new ResponseEntity<>(customerResponseList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCustomerById(Long id) {
        Optional<Customer> customer = repository.findById(id);

        if (customer.isPresent()) {
            return new ResponseEntity<>(createCustomerResponse(customer.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> updateCustomer(Long id, CustomerRequest request) {
        Optional<Customer> customerOptional = repository.findById(id);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            if (StringUtils.hasText(request.getName())) {
                customer.setName(request.getName());
            }

            if (StringUtils.hasText(request.getPassword())) {
                customer.setPassword(request.getPassword());
            }

            repository.save(customer);

            return new ResponseEntity<>(createCustomerResponse(customer), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        repository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private void checkCustomerName(String name) {
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("The username you have chosen is already taken.");
        }
    }

    private CustomerResponse createCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName());
    }

    private List<CustomerResponse> createCustomerResponseList(Collection<Customer> customer) {
        return customer.stream()
                .map(this::createCustomerResponse)
                .collect(Collectors.toList());
    }
}

package me.agno.demo.controllers;

import lombok.RequiredArgsConstructor;
import me.agno.demo.model.Customer;
import me.agno.demo.repositories.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping(value = {"/customer", "/Customer"})
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @PostMapping(value = {"/customer", "/Customer"})
    public ResponseEntity<Object> create(@RequestBody Customer customer) {

        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        return ResponseEntity.ok(savedCustomer.getCustomerID());
    }

    @GetMapping(value = {"/customer/{id}", "/Customer/{id}"})
    public ResponseEntity<Customer> get(@PathVariable String id) {

        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = {"/customer/{id}", "/Customer/{id}"})
    public ResponseEntity<Object> update(@RequestBody Customer customer, @PathVariable String id) {

        Optional<Customer> attachedCustomer = customerRepository.findById(id);
        if (attachedCustomer.isEmpty())
            return ResponseEntity.notFound().build();

        customer.setCustomerID(id);
        customerRepository.saveAndFlush(customer);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = {"/customer/{id}", "/Customer/{id}"})
    public ResponseEntity<Object> delete(@PathVariable String id) {

        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty())
            return ResponseEntity.notFound().build();

        customerRepository.delete(customer.get());
        customerRepository.flush();

        return ResponseEntity.noContent().build();
    }
}

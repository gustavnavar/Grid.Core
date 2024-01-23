package me.agno.demo.controllers;

import lombok.RequiredArgsConstructor;
import me.agno.demo.model.Order;
import me.agno.demo.repositories.CustomerRepository;
import me.agno.demo.repositories.EmployeeRepository;
import me.agno.demo.repositories.OrderRepository;
import me.agno.demo.repositories.ShipperRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipperRepository shipperRepository;

    @GetMapping(value = {"/order", "/Order"})
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping(value = {"/order", "/Order"})
    public ResponseEntity<Object> create(@RequestBody Order order) {

        try {
            if(order.getCustomerID() != null && ! order.getCustomerID().isBlank()) {
                var customer = customerRepository.findById(order.getCustomerID());
                customer.ifPresent(order::setCustomer);
            }
            if(order.getEmployeeID() != null) {
                var employee = employeeRepository.findById(order.getEmployeeID());
                employee.ifPresent(order::setEmployee);
            }
            if(order.getShipVia() != null) {
                var shipper = shipperRepository.findById(order.getShipVia());
                shipper.ifPresent(order::setShipper);
            }
            Order savedOrder = orderRepository.saveAndFlush(order);
            return ResponseEntity.ok(savedOrder.getOrderID());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }

    @GetMapping(value = {"/order/{id}", "/Order/{id}"})
    public ResponseEntity<Order> get(@PathVariable int id) {

        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = {"/order/{id}", "/Order/{id}"})
    public ResponseEntity<Object> update(@RequestBody Order order, @PathVariable int id) {

        Optional<Order> attachedOrder = orderRepository.findById(id);
        if (attachedOrder.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            order.setOrderID(id);
            if(order.getCustomerID() != null && ! order.getCustomerID().isBlank()
                    && !order.getCustomerID().equals(attachedOrder.get().getCustomerID())) {
                var customer = customerRepository.findById(order.getCustomerID());
                customer.ifPresent(order::setCustomer);
            }
            if(order.getEmployeeID() != null && !order.getEmployeeID().equals(attachedOrder.get().getEmployeeID())) {
                var employee = employeeRepository.findById(order.getEmployeeID());
                employee.ifPresent(order::setEmployee);
            }
            if(order.getShipVia() != null && !order.getShipVia().equals(attachedOrder.get().getShipVia())) {
                var shipper = shipperRepository.findById(order.getShipVia());
                shipper.ifPresent(order::setShipper);
            }
            orderRepository.saveAndFlush(order);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }

    @DeleteMapping(value = {"/order/{id}", "/Order/{id}"})
    public ResponseEntity<Object> delete(@PathVariable int id) {

        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            orderRepository.delete(order.get());
            orderRepository.flush();
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }
}

package me.agno.demo.controllers;

import lombok.RequiredArgsConstructor;
import me.agno.demo.model.Order;
import me.agno.demo.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping(value = {"/order", "/Order"})
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping(value = {"/order", "/Order"})
    public ResponseEntity<Object> create(@RequestBody Order order) {

        Order savedOrder = orderRepository.saveAndFlush(order);
        return ResponseEntity.ok(savedOrder.getOrderID());
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

        order.setOrderID(id);
        orderRepository.saveAndFlush(order);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = {"/order/{id}", "/Order/{id}"})
    public ResponseEntity<Object> delete(@PathVariable int id) {

        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty())
            return ResponseEntity.notFound().build();

        orderRepository.delete(order.get());
        orderRepository.flush();

        return ResponseEntity.noContent().build();
    }
}

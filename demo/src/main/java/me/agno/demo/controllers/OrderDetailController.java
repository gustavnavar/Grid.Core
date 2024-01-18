package me.agno.demo.controllers;

import lombok.RequiredArgsConstructor;
import me.agno.demo.model.OrderDetail;
import me.agno.demo.model.OrderDetailKey;
import me.agno.demo.repositories.OrderDetailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class OrderDetailController {

    private final OrderDetailRepository orderDetailRepository;

    @GetMapping(value = {"/orderdetail", "/OrderDetail"})
    public ResponseEntity<List<OrderDetail>> getAll() {
        return ResponseEntity.ok(orderDetailRepository.findAll());
    }

    @PostMapping(value = {"/orderdetail", "/OrderDetail"})
    public ResponseEntity<Object> create(@RequestBody OrderDetail orderDetail) {

        orderDetailRepository.saveAndFlush(orderDetail);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = {"/orderdetail/{orderId}/{productId}", "/OrderDetail/{orderId}/{productId}"})
    public ResponseEntity<OrderDetail> get(@PathVariable int orderId, @PathVariable int productId) {

        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(new OrderDetailKey(orderId, productId));
        return orderDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = {"/orderdetail/{orderId}/{productId}", "/OrderDetail/{orderId}/{productId}"})
    public ResponseEntity<Object> update(@RequestBody OrderDetail orderDetail,
                                         @PathVariable int orderId, @PathVariable int productId) {

        Optional<OrderDetail> attachedOrderDetail = orderDetailRepository.findById(new OrderDetailKey(orderId, productId));
        if (attachedOrderDetail.isEmpty())
            return ResponseEntity.notFound().build();

        orderDetail.setOrderID(orderId);
        orderDetail.setProductID(productId);
        orderDetailRepository.saveAndFlush(orderDetail);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = {"/orderdetail/{orderId}/{productId}", "/OrderDetail/{orderId}/{productId}"})
    public ResponseEntity<Object> delete(@PathVariable int orderId, @PathVariable int productId) {

        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(new OrderDetailKey(orderId, productId));
        if (orderDetail.isEmpty())
            return ResponseEntity.notFound().build();

        orderDetailRepository.delete(orderDetail.get());
        orderDetailRepository.flush();

        return ResponseEntity.noContent().build();
    }
}

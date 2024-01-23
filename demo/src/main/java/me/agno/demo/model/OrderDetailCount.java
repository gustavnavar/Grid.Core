package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "\"order details\"")
@IdClass(OrderDetailKey.class)
public class OrderDetailCount {

    @Id
    @Column(name = "orderid", nullable = false)
    private Integer orderID;

    @Id
    @Column(name = "productid", nullable = false)
    private Integer productID;

    @Column(name = "unitprice", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Short quantity;

    @Column(name = "discount", nullable = false)
    private Float discount;

}
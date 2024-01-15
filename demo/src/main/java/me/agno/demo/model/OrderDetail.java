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
public class OrderDetail {
    @EmbeddedId
    private OrderDetailId id;

    @JsonIgnore
    @MapsId("orderID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderid", nullable = false)
    private Order orderID;

    @JsonIgnore
    @MapsId("productID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product productID;

    @Column(name = "unitprice", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Short quantity;

    @Column(name = "discount", nullable = false)
    private Float discount;

}
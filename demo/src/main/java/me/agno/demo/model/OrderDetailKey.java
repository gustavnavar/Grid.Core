package me.agno.demo.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderDetailKey implements Serializable {
    private Integer orderID;
    private Integer productID;

    public OrderDetailKey(int orderID, int productID) {
        this.orderID = orderID;
        this.productID = productID;
    }
}

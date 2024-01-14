package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private Integer orderID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid")
    private Customer customerID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private Employee employeeID;

    @Column(name = "orderdate")
    private Instant orderDate;

    @Column(name = "requireddate")
    private Instant requiredDate;

    @Column(name = "shippeddate")
    private Instant shippedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipvia")
    private Shipper shipVia;

    @Column(name = "freight")
    private BigDecimal freight;

    @Nationalized
    @Column(name = "shipname", length = 40)
    private String shipName;

    @Nationalized
    @Column(name = "shipaddress", length = 60)
    private String shipAddress;

    @Nationalized
    @Column(name = "shipcity", length = 15)
    private String shipCity;

    @Nationalized
    @Column(name = "shipregion", length = 15)
    private String shipRegion;

    @Nationalized
    @Column(name = "shippostalcode", length = 10)
    private String shipPostalCode;

    @Nationalized
    @Column(name = "shipcountry", length = 15)
    private String shipCountry;

    @JsonIgnore
    @OneToMany(mappedBy = "orderID")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}
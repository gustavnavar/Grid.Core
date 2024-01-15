package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "customerid", insertable = false, updatable = false, nullable = false)
    private String customerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerid")
    private Customer customer;

    @Column(name = "employeeid", columnDefinition = "int", insertable = false, updatable = false, nullable = false)
    private String employeeID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeid")
    private Employee employee;

    @Column(name = "orderdate")
    private Instant orderDate;

    @Column(name = "requireddate")
    private Instant requiredDate;

    @Column(name = "shippeddate")
    private Instant shippedDate;

    @Column(name = "shipVia", columnDefinition = "int", insertable = false, updatable = false, nullable = false)
    private String shipVia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipVia")
    private Shipper shipper;

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
package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.agno.gridjavacore.annotations.GridColumn;
import me.agno.gridjavacore.annotations.GridTable;
import me.agno.gridjavacore.pagination.PagingType;
import me.agno.gridjavacore.sorting.GridSortDirection;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
@GridTable(pagingType = PagingType.PAGINATION, pageSize = 20)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    @GridColumn(position = 0, type = Integer.class)
    private Integer orderID;

    @Column(name = "customerid", insertable = false, updatable = false, nullable = false)
    @GridColumn(position = 5, type = String.class)
    private String customerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerid")
    private Customer customer;

    @Column(name = "employeeid", columnDefinition = "int", insertable = false, updatable = false, nullable = false)
    @GridColumn(position = 4, type = Integer.class)
    private Integer employeeID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeid")
    private Employee employee;

    @Column(name = "orderdate")
    @GridColumn(position = 1, type = LocalDateTime.class, sortEnabled = true, filterEnabled = true, sortInitialDirection = GridSortDirection.ASCENDING)
    private LocalDateTime orderDate;

    @Column(name = "requireddate")
    @GridColumn(position = 2, type = LocalDateTime.class)
    private LocalDateTime requiredDate;

    @Column(name = "shippeddate")
    @GridColumn(position = 3, type = LocalDateTime.class)
    private LocalDateTime shippedDate;

    @Column(name = "shipVia", columnDefinition = "int", insertable = false, updatable = false, nullable = false)
    @GridColumn(position = 6, type = Integer.class)
    private Integer shipVia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipVia")
    private Shipper shipper;

    @Column(name = "freight")
    @GridColumn(position = 7, type = BigDecimal.class, sortEnabled = true, filterEnabled = true)
    private Double freight;

    @Nationalized
    @Column(name = "shipname", length = 40)
    @GridColumn(position = 8, type = String.class)
    private String shipName;

    @Nationalized
    @Column(name = "shipaddress", length = 60)
    @GridColumn(position = 9, type = String.class)
    private String shipAddress;

    @Nationalized
    @Column(name = "shipcity", length = 15)
    @GridColumn(position = 10, type = String.class)
    private String shipCity;

    @Nationalized
    @Column(name = "shipregion", length = 15)
    @GridColumn(position = 11, type = String.class)
    private String shipRegion;

    @Nationalized
    @Column(name = "shippostalcode", length = 10)
    @GridColumn(position = 12, type = String.class)
    private String shipPostalCode;

    @Nationalized
    @Column(name = "shipcountry", length = 15)
    @GridColumn(position = 13, type = String.class)
    private String shipCountry;

    @JsonIgnore
    @OneToMany(mappedBy = "orderID")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    public Order (double freight) {
        this.freight = freight;
    }
}
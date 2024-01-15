package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "productname", nullable = false, length = 40)
    private String productName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private Supplier supplierID;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private Category categoryID;

    @Nationalized
    @Column(name = "quantityperunit", length = 20)
    private String quantityPerUnit;

    @Column(name = "unitprice")
    private BigDecimal unitPrice;

    @Column(name = "unitsinstock")
    private Short unitsInStock;

    @Column(name = "unitsonorder")
    private Short unitsOnOrder;

    @Column(name = "reorderlevel")
    private Short reorderLevel;

    @Column(name = "discontinued", nullable = false)
    private Boolean discontinued = false;

    @JsonIgnore
    @OneToMany(mappedBy = "productID")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}
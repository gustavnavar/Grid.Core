package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Nationalized
    @Column(name = "customerid", nullable = false, length = 5)
    private String customerID;

    @Nationalized
    @Column(name = "companyname", nullable = false, length = 40)
    private String companyName;

    @Nationalized
    @Column(name = "contactname", length = 30)
    private String contactName;

    @Nationalized
    @Column(name = "contacttitle", length = 30)
    private String contactTitle;

    @Nationalized
    @Column(name = "address", length = 60)
    private String address;

    @Nationalized
    @Column(name = "city", length = 15)
    private String city;

    @Nationalized
    @Column(name = "region", length = 15)
    private String region;

    @Nationalized
    @Column(name = "postalcode", length = 10)
    private String postalCode;

    @Nationalized
    @Column(name = "country", length = 15)
    private String country;

    @Nationalized
    @Column(name = "phone", length = 24)
    private String phone;

    @Nationalized
    @Column(name = "fax", length = 24)
    private String fax;

    @Column(name = "isvip", nullable = false)
    private Boolean isVip = false;

    @JsonIgnore
    @OneToMany(mappedBy = "customerID")
    private Set<Order> orders = new LinkedHashSet<>();

}
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
@Table(name = "shippers")
public class Shipper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipperid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "companyname", nullable = false, length = 40)
    private String companyName;

    @Nationalized
    @Column(name = "phone", length = 24)
    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "shipVia")
    private Set<Order> orders = new LinkedHashSet<>();

}
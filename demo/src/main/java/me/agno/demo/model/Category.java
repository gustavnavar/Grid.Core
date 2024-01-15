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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "categoryname", nullable = false, length = 15)
    private String categoryName;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "picture")
    private byte[] picture;

    @JsonIgnore
    @OneToMany(mappedBy = "categoryID")
    private Set<Product> products = new LinkedHashSet<>();

}
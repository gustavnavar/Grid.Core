package me.agno.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Region {
    @Id
    @Column(name = "regionid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "regiondescription", nullable = false, length = 50)
    private String regionDescription;

    @JsonIgnore
    @OneToMany(mappedBy = "regionID")
    private Set<Territory> territories = new LinkedHashSet<>();

}
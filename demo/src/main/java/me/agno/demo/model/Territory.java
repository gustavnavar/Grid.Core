package me.agno.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "territories")
public class Territory {
    @Id
    @Nationalized
    @Column(name = "territoryid", nullable = false, length = 20)
    private String territoryID;

    @Nationalized
    @Column(name = "territorydescription", nullable = false, length = 50)
    private String territoryDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "regionid", nullable = false)
    private Region regionID;

}
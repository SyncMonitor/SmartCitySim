package it.synclab.smartcitysim.maintainerregistry.entities;

import java.util.List;

import it.synclab.smartcitysim.sensor.entities.Sensor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "maintainers_registry")
public class MaintainerRegistry implements it.synclab.smartcitysim.interfaces.Entity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(
        nullable = false,
        unique = true
    )
    private String company;

    @Column(
        nullable = false,
        unique = true
    )
    private String phone;

    @Column(
        nullable = false,
        unique = true
    )
    private String email;

    @OneToMany(mappedBy = "maintainer")
    private List<Sensor> sensors;
}

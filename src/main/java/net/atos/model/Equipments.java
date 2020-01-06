package net.atos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Equipments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipments_id;

    private String equipmentDescription;

    @ManyToMany(mappedBy = "equipments")
    //private Set<Car> car;
    private List<Car> car;

    public Equipments(String equipmentDescription) {

        this.equipmentDescription = equipmentDescription;
    }


}

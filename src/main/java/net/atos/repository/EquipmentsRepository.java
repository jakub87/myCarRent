package net.atos.repository;


import net.atos.model.Equipments;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentsRepository extends JpaRepository<Equipments,Long> {
    boolean existsByEquipmentDescription(String equipmentDescription);

}

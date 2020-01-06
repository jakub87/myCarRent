package net.atos.repository;

import net.atos.model.Role;
import net.atos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

}

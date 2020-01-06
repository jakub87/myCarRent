package net.atos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    private String roleName;

    //@ManyToMany(mappedBy = "roles")
    @ManyToMany (mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public String getRoleName() {
        return roleName;
    }
}

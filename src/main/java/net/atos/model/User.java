package net.atos.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.atos.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

   private boolean status = true;

    private String city;

    private Integer mobileNumber;

    private LocalDate creationDate = LocalDate.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> order;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="USER_ID"),
            inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public boolean isAdmin()
    {

       Optional<Role> role_admin = this.getRoles().stream()
                                                  .filter(role -> role.getRoleName().equalsIgnoreCase("role_admin"))
                                                  .findFirst();

        if (role_admin.isPresent())
        {
            return true;
        }
        else
        {
            return false;
        }

    }


    public void addRole(Role role)
    {
        this.roles.add(role);
    }

    public void removeRole(Role role)
    {
        this.roles.remove(role);
    }
//
//    @Override
//    public int compareTo(User user)
//    {
//        return this.lastName.compareToIgnoreCase(user.getLastName());
//    }
}

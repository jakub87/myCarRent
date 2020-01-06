package net.atos.repository;

import net.atos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //wyszukuje liste wszyskich mejli
    @Query(value = "SELECT c.email FROM user c", nativeQuery = true)
    List<String> findAllEmails();

    Page<User> findAllByFirstNameContainsOrLastNameContainsOrEmailContains(String firstName, String lastName, String email, Pageable pageable);

    User findFirstByEmail(String email);

}

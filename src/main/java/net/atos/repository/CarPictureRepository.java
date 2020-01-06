package net.atos.repository;

import net.atos.model.CarPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CarPictureRepository extends JpaRepository<CarPicture,Long> {
    boolean existsCarPictureByPathOfURL(String pathOfURL);

    }

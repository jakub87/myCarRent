package net.atos.repository;

import net.atos.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    @Query(value = "SELECT c FROM Car c where c.mark like %?1% and c.model like %?2% and c.year between ?3 and ?4 and c.price between ?5 and ?6")
    Page <Car> searchCarsByFilters(String mark, String model, int yearFrom, int yearTo, BigDecimal priceFrom, BigDecimal priceTo, Pageable pageable);

    @Query(value ="SELECT * FROM car WHERE hot_offer = 'YES'"  , nativeQuery = true)
    Page <Car> findCarsWithSpecialOffers(Pageable pageable);



}

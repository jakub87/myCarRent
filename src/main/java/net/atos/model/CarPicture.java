package net.atos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CarPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carPicture_id;

    private String pathOfURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id")
    private Car car;

    public CarPicture(String pathOfURL) {
        this.pathOfURL = pathOfURL;

    }

    public CarPicture(String pathOfURL, Car car) {
        this.pathOfURL = pathOfURL;
        this.car = car;
    }
}

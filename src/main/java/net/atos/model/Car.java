package net.atos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.atos.model.enums.Color;
import net.atos.model.enums.HotOffer;
import net.atos.model.enums.Status;
import net.atos.model.enums.Type;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long car_id;

    private String mark;

    private String model;

    @Enumerated(EnumType.STRING)
    private Type type;

    private int year;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private Status status  = Status.AVAILABLE;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private HotOffer hotOffer;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "car",
            cascade = CascadeType.ALL)
    private List<CarPicture> pictures = new ArrayList<>();
   // private List<CarPicture> pictures  = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER) //(cascade = CascadeType.ALL)  // (cascade = CascadeType.PERSIST)   //@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
       name ="car_equipments",
       joinColumns = @JoinColumn(name ="car_id"),
       inverseJoinColumns = @JoinColumn(name = "carEquipments_id")
    )
    private List<Equipments> equipments; // = new ArrayList<>();

    //@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car", cascade = CascadeType.ALL) // tutaj chyba nie powinno byc cascade all
    private List<Order> orders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car", cascade = CascadeType.ALL)
    private List<Comment> comments;


    public Car(String mark, String model, Type type, int year, Color color, BigDecimal price, HotOffer hotOffer) {
        this.mark = mark;
        this.model = model;
        this.type = type;
        this.year = year;
        this.color = color;
        this.price = price;
        this.hotOffer = hotOffer;
    }

    public void addEquipment(Equipments equipments)
    {
        this.equipments.add(equipments);
    }

    public void addImage(CarPicture carPicture)
    {
        this.pictures.add(carPicture);
    }

    public void removeImage(CarPicture carPicture)
    {
        this.pictures.remove(carPicture);
    }

    public void removeEquipment(Equipments equipments)
    {

        this.equipments.remove(equipments);
    }

}

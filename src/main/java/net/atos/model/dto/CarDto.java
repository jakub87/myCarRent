package net.atos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.atos.model.enums.Color;
import net.atos.model.enums.HotOffer;
import net.atos.model.enums.Status;
import net.atos.model.enums.Type;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    @Pattern(regexp = "^[a-zA-Z- ]{2,20}$",message = "Invalid characters / size must be 2 - 20.")
    private String mark;

    @Pattern(regexp = "^[a-zA-Z- 0-9]{1,20}$", message = "Invalid characters / size must be 1 - 20.")
    private String model;

    private Type type;

    @Min(value = 1980, message = "Year must be > 1980.")
    private int year = LocalDate.now().getYear();
    private Color color;

    @NotNull(message = "Price cannot be empty.")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0.")
    @DecimalMax(value = "99999.9", message = "Price must be less than 100000.")
    private BigDecimal price;

    private List<CarPictureDto> pictures = new ArrayList<>();
    private List<EquipmentsDto> equipments = new ArrayList<>();
    private Status status;
    private HotOffer hotOffer;
    private MultipartFile imageFile;
}

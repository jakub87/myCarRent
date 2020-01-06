package net.atos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.atos.model.Car;
import net.atos.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {

    private Car car;
    private User author;
    private String content;

}

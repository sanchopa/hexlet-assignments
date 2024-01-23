package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductUpdateDTO {
    private long id;
    private String title;
    private int price;
}

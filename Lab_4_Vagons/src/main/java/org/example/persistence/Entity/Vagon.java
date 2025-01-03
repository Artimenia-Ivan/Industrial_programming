package org.example.persistence.Entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Vagon {
    Integer cost;
    String name;
    String description;


    public Vagon(Integer cost, String name, String Desc) {
        this.name = name;
        this.cost = cost;
        this.description = Desc;
    }


    @Override
    public String toString() {
        return "Вагон" +
                "Цена " + cost +
                " Имя " + name +
                " описание " + description + ' ';
    }
}

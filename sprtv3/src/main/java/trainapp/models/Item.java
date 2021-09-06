package trainapp.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Item {

    @Setter(AccessLevel.NONE)
    private int id;
    private int size;
    private int price;
    private String type;
    private String model;
    private String color;

    public Item(int id, int size, int price, String type, String model, String color) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.type = type;
        this.model = model;
        this.color = color;
    }
}

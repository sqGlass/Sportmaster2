package trainapp.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder(toBuilder = true)
public class Item {

    @Setter(AccessLevel.NONE)
    private Integer id;
    private Integer size;
    private Integer price;
    private String type;
    private String model;
    private String color;

}

package model;

import java.util.Objects;

public class Item {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && size == item.size && price == item.price && Objects.equals(type, item.type) && Objects.equals(model, item.model) && Objects.equals(color, item.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, price, type, model, color);
    }
}

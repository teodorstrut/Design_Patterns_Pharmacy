package Models;

import java.util.ArrayList;

public class Product extends BaseObject {
    private double price;
    private String productName;
    private String description;
    private int quantity;

    public Product(int id, double price, String productName, String description, int quantity) {
        super(id);
        this.price = price;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
    }

    public static Product fromCSV(String line) {
        String[] items = line.split(";");
        return new Product(
                Integer.parseInt(items[0]),
                Double.parseDouble(items[1]),
                items[2],
                items[3],
                Integer.parseInt(items[4])
        );
    }

    public String toCSV() {
        return this.getId() + ";" + this.price + ";" + this.productName + ";" + this.description + ";" + this.quantity;
    }

    public double getPrice() {
        return price;
    }


    public String getDescription() {
        return description;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + this.getId() +
                ", price=" + price +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product clone() {
        return new Product(
                this.getId(),
                this.getPrice(),
                this.getProductName(),
                this.getDescription(),
                this.getQuantity()
        );
    }
}

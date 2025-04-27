package org.utkrisht.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.utkrisht.product.model.Category;
import org.utkrisht.product.model.ProductImage;

import java.util.List;

public class ProductDto {

    private Long id;

    @NotBlank(message = "Product Name can't be blank.")
    @Size(min = 3,message = "Product should be minimum of 3 characters")
    private String productName;

    @NotBlank(message = "Description is necessary.")
    @Size(min=6,message = "Minimum 6 characters")
    private String description;

    private List<ProductImage> images;


    private double price;

    private double discount;

    private Integer quantity;

    private double specialPrice;

    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

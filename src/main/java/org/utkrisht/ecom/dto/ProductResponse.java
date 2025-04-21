package org.utkrisht.ecom.dto;

public class ProductResponse {
    
    private ProductDto data;
    private String status;

    public ProductResponse(ProductDto data, String status) {
        this.data = data;
        this.status = status;
    }

    public ProductDto getData() {
        return data;
    }

    public void setData(ProductDto data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.example.fire_base_demo.Model;

public class Product_Data
{
    String id;
    String pName;
    String pPrice;
    String pDes;
    String pImgUrl;

    public Product_Data(String id, String pName, String pPrice, String pDes,String pImgUrl) {
        this.id = id;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pDes = pDes;
        this.pImgUrl = pImgUrl;
    }
    public Product_Data() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpDes() {
        return pDes;
    }

    public void setpDes(String pDes) {
        this.pDes = pDes;
    }
    public String getpImgUrl() {
        return pImgUrl;
    }

    public void setpImgUrl(String pImgUrl) {
        this.pImgUrl = pImgUrl;
    }

    @Override
    public String toString() {
        return "Product_Data{" +
                "id='" + id + '\'' +
                ", pName='" + pName + '\'' +
                ", pPrice='" + pPrice + '\'' +
                ", pDes='" + pDes + '\'' +
                ", pImgUrl='" + pImgUrl + '\'' +
                '}';
    }
}



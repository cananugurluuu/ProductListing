package com.example.cci.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by SONY on 14.08.2017.
 */

public class Product implements Serializable {

    private String code ;
    private String productNumber;
    private String name;
    private String advicedPrice;
    private String category;
    private String countryCode;

    public Product(){

    }
    public Product(JSONObject jsonobject) throws JSONException {
        this.code=jsonobject.getString("Code");
        this.productNumber=jsonobject.getString("ProductNumber");
        this.name=jsonobject.getString("Name");
        this.advicedPrice=jsonobject.getString("AdvicedPrice");
        this.category=jsonobject.getString("Category");
        this.countryCode=jsonobject.getString("CountryCode");
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getProductNumber() {
        return productNumber;
    }
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAdvicedPrice() {
        return advicedPrice;
    }
    public void setAdvicedPrice(String advicedPrice) {
        this.advicedPrice = advicedPrice;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

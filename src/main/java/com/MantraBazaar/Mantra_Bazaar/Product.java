package com.MantraBazaar.Mantra_Bazaar;

import java.io.Serializable;

public class Product implements Serializable{
    private String Name;
    private String Description;
    private float CostPrice;
    private String TaxRate;
    private float CostIncTax;
    private float SalePriceIncTax;
    private boolean isVariablePrice;
    private double MarginPerc;
    private long Barcode;
    private String Category;
    private long ProductId;
    
    public long getProductId() {
        return ProductId;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        this.Description = description;
    }
    public float getCostPrice() {
        return CostPrice;
    }
    public void setCostPrice(float costPrice) {
        this.CostPrice = costPrice;
    }
    public String getTaxRate() {
        return TaxRate;
    }
    public void setTaxRate(String taxRate) {
        this.TaxRate = taxRate;
    }
    public float getCostIncTax() {
        return CostIncTax;
    }
    public void setCostIncTax(float costIncTax) {
        this.CostIncTax = costIncTax;
    }
    public float getSalePriceIncTax() {
        return SalePriceIncTax;
    }
    public void setSalePriceIncTax(float salePriceIncTax) {
        this.SalePriceIncTax = salePriceIncTax;
    }
    public boolean isVariablePrice() {
        return isVariablePrice;
    }
    public void setVariablePrice(boolean isVariablePrice) {
        this.isVariablePrice = isVariablePrice;
    }
    public double getMarginPerc() {
        return MarginPerc;
    }
    public void setMarginPerc(double marginPerc) {
        this.MarginPerc = marginPerc;
    }
    public long getBarcode() {
        return Barcode;
    }
    public void setBarcode(long barcode) {
        this.Barcode = barcode;
    }
    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        this.Category = category;
    }
    public void setProductId(long productId) {
        this.ProductId = productId;
    }

}

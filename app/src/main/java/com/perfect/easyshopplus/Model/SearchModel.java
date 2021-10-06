package com.perfect.easyshopplus.Model;

public class SearchModel {

    private String ItemName;
    private String MRP;
    private String SalesPrice;
    private String ID_Items;
    private String ID_Stock;
    private String CurrentStock;
    private String RetailPrice;
    private String PrivilagePrice;
    private String WholesalePrice;
    private String GST;
    private String CESS;
    private String Description;
    private String ImageName;
    private String Packed;
    private String ItemMalayalamName;

    public SearchModel(String ItemName, String MRP, String SalesPrice, String ID_Items, String ID_Stock, String CurrentStock,
                       String RetailPrice, String PrivilagePrice, String WholesalePrice,
                       String GST, String CESS, String Description, String ImageName, String Packed, String ItemMalayalamName) {
        this.ItemName = ItemName;
        this.MRP = MRP;
        this.SalesPrice = SalesPrice;
        this.ID_Items = ID_Items;
        this.ID_Stock = ID_Stock;
        this.CurrentStock = CurrentStock;
        this.RetailPrice = RetailPrice;
        this.PrivilagePrice = PrivilagePrice;
        this.WholesalePrice = WholesalePrice;
        this.GST = GST;
        this.CESS = CESS;
        this.Description = Description;
        this.ImageName = ImageName;
        this.Packed = Packed;
        this.ItemMalayalamName = ItemMalayalamName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        SalesPrice = salesPrice;
    }

    public String getID_Items() {
        return ID_Items;
    }

    public void setID_Items(String ID_Items) {
        this.ID_Items = ID_Items;
    }

    public String getID_Stock() {
        return ID_Stock;
    }

    public void setID_Stock(String ID_Stock) {
        this.ID_Stock = ID_Stock;
    }

    public String getCurrentStock() {
        return CurrentStock;
    }

    public void setCurrentStock(String currentStock) {
        CurrentStock = currentStock;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        RetailPrice = retailPrice;
    }

    public String getPrivilagePrice() {
        return PrivilagePrice;
    }

    public void setPrivilagePrice(String privilagePrice) {
        PrivilagePrice = privilagePrice;
    }

    public String getWholesalePrice() {
        return WholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        WholesalePrice = wholesalePrice;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getCESS() {
        return CESS;
    }

    public void setCESS(String CESS) {
        this.CESS = CESS;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getPacked() {
        return Packed;
    }

    public void setPacked(String packed) {
        Packed = packed;
    }

    public String getItemMalayalamName() {
        return ItemMalayalamName;
    }

    public void setItemMalayalamName(String itemMalayalamName) {
        ItemMalayalamName = itemMalayalamName;
    }
}

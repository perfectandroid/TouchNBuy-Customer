package com.perfect.easyshopplus.Model;

public class CartModel {

    private String ItemName;
    private String SalesPrice;
    private String MRP;
    private String ID_Items;
    private String Stock_ID;
    private String QTY;
    private String RetailPrice;
    private String PrivilagePrice;
    private String WholesalePrice;
    private String GST;
    private String CESS;
    private String Count;
    private String Packed;
    private String Description;
    private String ImageName;
    private String ItemMalayalamName;

    public CartModel(){}

    public CartModel(String ID_Items, String ItemName, String SalesPrice, String MRP, String Stock_ID, String QTY,
                     String RetailPrice, String PrivilagePrice, String WholesalePrice, String GST, String CESS,
                     String Count, String Packed, String Description, String ImageName, String ItemMalayalamName) {
        this.ItemName = ItemName;
        this.SalesPrice = SalesPrice;
        this.MRP = MRP;
        this.ID_Items = ID_Items;
        this.Stock_ID = Stock_ID;
        this.QTY = QTY;
        this.RetailPrice = RetailPrice;
        this.PrivilagePrice = PrivilagePrice;
        this.WholesalePrice = WholesalePrice;
        this.GST = GST;
        this.CESS = CESS;
        this.Count = Count;
        this.Packed = Packed;
        this.Description = Description;
        this.ImageName = ImageName;
        this.ItemMalayalamName = ItemMalayalamName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        SalesPrice = salesPrice;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getID_Items() {
        return ID_Items;
    }

    public void setID_Items(String ID_Items) {
        this.ID_Items = ID_Items;
    }

    public String getStock_ID() {
        return Stock_ID;
    }

    public void setStock_ID(String stock_ID) {
        Stock_ID = stock_ID;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
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

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getPacked() {
        return Packed;
    }

    public void setPacked(String packed) {
        Packed = packed;
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

    public String getItemMalayalamName() {
        return ItemMalayalamName;
    }

    public void setItemMalayalamName(String itemMalayalamName) {
        ItemMalayalamName = itemMalayalamName;
    }
}

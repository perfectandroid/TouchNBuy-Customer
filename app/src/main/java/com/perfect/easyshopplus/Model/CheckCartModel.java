package com.perfect.easyshopplus.Model;

public class CheckCartModel {

    private String Itemid;
    private String StockId;
    private String QTY;
    private String StoreId;

    public CheckCartModel(){}

    public CheckCartModel(String Itemid, String StockId, String QTY, String StoreId) {
        this.Itemid = Itemid;
        this.StockId = StockId;
        this.QTY = QTY;
        this.StoreId = StoreId;
    }

    public String getItemid() {
        return Itemid;
    }

    public void setItemid(String itemid) {
        Itemid = itemid;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String stockId) {
        StockId = stockId;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

}

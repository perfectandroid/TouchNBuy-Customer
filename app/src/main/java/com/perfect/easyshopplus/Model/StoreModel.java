package com.perfect.easyshopplus.Model;

public class StoreModel {

    private String ID_Store;
    private String StoreName;

    public StoreModel(String ID_Store, String StoreName) {
        this.StoreName = StoreName;
        this.ID_Store = ID_Store;
    }

    public String getID_Store() {
        return ID_Store;
    }

    public void setID_Store(String ID_Store) {
        this.ID_Store = ID_Store;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }
}

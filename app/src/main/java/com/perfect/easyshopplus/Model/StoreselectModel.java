package com.perfect.easyshopplus.Model;

public class StoreselectModel {

    private String ID_Store;
    private String StoreName;
    private String ImagePath;

    public StoreselectModel(String ID_Store, String StoreName, String ImagePath) {
        this.StoreName = StoreName;
        this.ID_Store = ID_Store;
        this.ImagePath = ImagePath;
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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}

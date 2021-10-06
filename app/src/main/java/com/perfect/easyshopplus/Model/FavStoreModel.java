package com.perfect.easyshopplus.Model;

public class FavStoreModel {

    private String ID_Items;
    private String ItemName;
    private String ImageName;

    public FavStoreModel(){}

    public FavStoreModel(String ID_Items, String ItemName, String ImageName) {
        this.ItemName = ItemName;
        this.ID_Items = ID_Items;
        this.ImageName = ImageName;
    }

    public String getID_Items() {
        return ID_Items;
    }

    public void setID_Items(String ID_Items) {
        this.ID_Items = ID_Items;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}

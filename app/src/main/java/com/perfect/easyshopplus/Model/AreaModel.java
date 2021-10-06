package com.perfect.easyshopplus.Model;

public class AreaModel {

    private String ID_Area;
    private String AreaName;

    public AreaModel(String ID_Area, String AreaName) {
        this.AreaName = AreaName;
        this.ID_Area = ID_Area;
    }

    public String getID_Area() {
        return ID_Area;
    }

    public void setID_Area(String ID_Area) {
        this.ID_Area = ID_Area;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }
}

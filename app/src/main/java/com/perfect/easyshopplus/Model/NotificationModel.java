package com.perfect.easyshopplus.Model;

public class NotificationModel {

    private String ID_Notification;
    private String Title;
    private String Description;
    private String IsRead;

    public NotificationModel(){}

    public NotificationModel(String ID_Notification, String Title, String Description, String IsRead) {
        this.ID_Notification = ID_Notification;
        this.Title = Title;
        this.Description = Description;
        this.IsRead = IsRead;
    }

    public String getID_Notification() {
        return ID_Notification;
    }

    public void setID_Notification(String ID_Notification) {
        this.ID_Notification = ID_Notification;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

}

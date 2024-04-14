package Adapters;

import android.widget.ImageView;

public class ItemSetGet {
    private  String itemName;
    private String ownerPhoneNumber;
    private String ownerEmail;
    private String itemType;
    private String ownerID;
    private String itemLocation;
    private String itemOwner;
    private  String itemUploadDate;
    private String itemWeight;
    private String itemImage;
    private String itemUniqueID;

    public ItemSetGet(String itemName, String ownerPhoneNumber, String ownerEmail, String itemType, String ownerID, String itemLocation, String itemOwner, String itemUploadDate, String itemWeight, String itemImage, String itemUniqueID) {
        this.itemName = itemName;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.ownerEmail = ownerEmail;
        this.itemType = itemType;
        this.ownerID = ownerID;
        this.itemLocation = itemLocation;
        this.itemOwner = itemOwner;
        this.itemUploadDate = itemUploadDate;
        this.itemWeight = itemWeight;
        this.itemImage = itemImage;
        this.itemUniqueID = itemUniqueID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(String itemOwner) {
        this.itemOwner = itemOwner;
    }

    public String getItemUploadDate() {
        return itemUploadDate;
    }

    public void setItemUploadDate(String itemUploadDate) {
        this.itemUploadDate = itemUploadDate;
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemUniqueID() {
        return itemUniqueID;
    }

    public void setItemUniqueID(String itemUniqueID) {
        this.itemUniqueID = itemUniqueID;
    }
}

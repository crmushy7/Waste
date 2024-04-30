package Adapters;

public class NotificationSetGet {
    private String title;
    private String message;

    private String notificationStatus;
    private String sentTime;
    private String collectorID;
    private String collectorPhone;
    private String collectorFCM;
    private String itemType;
    private String itemName;
    private String notificationID;
    private String itemID;
    private String collectorName;
    private String itemrequestStatus;

    public NotificationSetGet(String title, String message, String notificationStatus, String sentTime, String collectorID, String collectorPhone, String collectorFCM, String itemType, String itemName, String notificationID, String itemID, String collectorName, String itemrequestStatus) {
        this.title = title;
        this.message = message;
        this.notificationStatus = notificationStatus;
        this.sentTime = sentTime;
        this.collectorID = collectorID;
        this.collectorPhone = collectorPhone;
        this.collectorFCM = collectorFCM;
        this.itemType = itemType;
        this.itemName = itemName;
        this.notificationID = notificationID;
        this.itemID = itemID;
        this.collectorName = collectorName;
        this.itemrequestStatus = itemrequestStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public String getCollectorPhone() {
        return collectorPhone;
    }

    public void setCollectorPhone(String collectorPhone) {
        this.collectorPhone = collectorPhone;
    }

    public String getCollectorFCM() {
        return collectorFCM;
    }

    public void setCollectorFCM(String collectorFCM) {
        this.collectorFCM = collectorFCM;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getItemrequestStatus() {
        return itemrequestStatus;
    }

    public void setItemrequestStatus(String itemrequestStatus) {
        this.itemrequestStatus = itemrequestStatus;
    }
}

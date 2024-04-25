package Adapters;

public class NotificationSetGet {
    private String title;
    private String message;

    private String notificationStatus;
    private String sentTime;

    public NotificationSetGet(String title, String message, String notificationStatus, String sentTime) {
        this.title = title;
        this.message = message;
        this.notificationStatus = notificationStatus;
        this.sentTime = sentTime;
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
}

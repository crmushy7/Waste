package Classes;

public class Message {
    public static String SEND_BY_ME = "me";
    public static String SEND_BY_BOT = "bot";
    private final String message;
    private final String sendBy;

    public Message(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }


    public String getMessage() {
        return message;
    }

    public String getSendBy() {
        return sendBy;
    }

}

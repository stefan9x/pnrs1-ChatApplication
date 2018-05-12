package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sSender;
    private String sMessage;

    public MessageClass(String sSender, String sMessage) {
        this.sSender = sSender;
        this.sMessage = sMessage;
    }

    public String getsMessage() {
        return sMessage;
    }

    public String getsSender() {
        return sSender;
    }
}


package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessage;

    public MessageClass(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }
}

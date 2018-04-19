package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessage;
    private String sSenderId;
    private String sReceiverId;

    public MessageClass(String sMessage, String sSenderId, String sReceiverId) {
        this.sMessage = sMessage;
        this.sSenderId = sSenderId;
        this.sReceiverId = sReceiverId;
    }

    public String getsMessage() {
        return sMessage;
    }

    public String getsSenderId() {
        return sSenderId;
    }

    public String getsReceiverId() {
        return sReceiverId;
    }
}


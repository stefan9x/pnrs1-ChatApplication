package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessageId;
    private String sSenderId;
    private String sReceiverId;
    private String sMessage;

    public MessageClass(String sMessageId, String sSenderId, String sReceiverId, String sMessage) {
        this.sMessageId = sMessageId;
        this.sSenderId = sSenderId;
        this.sReceiverId = sReceiverId;
        this.sMessage = sMessage;
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

    public String getsId() {
        return sMessageId;
    }
}


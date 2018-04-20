package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessage;
    private String sSenderId;
    private String sReceiverId;
    private String sId;

    public MessageClass(String sId, String sSenderId, String sReceiverId, String sMessage) {
        this.sId = sId;
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
        return sId;
    }
}


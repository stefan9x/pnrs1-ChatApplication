package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessage;
    private String sSenderId;
    private String sReceiverId;
    private String sId;

    public MessageClass(String sMessage, String sSenderId, String sReceiverId, String sId) {
        this.sMessage = sMessage;
        this.sSenderId = sSenderId;
        this.sReceiverId = sReceiverId;
        this.sId = sId;
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


package stefan.jovanovic.chatapplication;

public class MessageClass {

    private String sMessage;
    private String sUser;

    public MessageClass(String sMessage, String sUser) {

        this.sMessage = sMessage;
        this.sUser = sUser;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsUser() {
        return sUser;
    }

    public void setsUser(String sUser) {
        this.sUser = sUser;
    }
}


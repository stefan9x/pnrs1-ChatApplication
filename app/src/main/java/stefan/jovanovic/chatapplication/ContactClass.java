package stefan.jovanovic.chatapplication;

public class ContactClass {

    private String sUserName;
    private String sLastMsg;

    public ContactClass(String sUserName, String sLastMsg) {
        this.sUserName = sUserName;
        this.sLastMsg = sLastMsg;
    }

    public String getsUserName() {
        return sUserName;
    }

    public String getsLastMsg() {
        return sLastMsg;
    }
}

package stefan.jovanovic.chatapplication;

public class ContactClass {
    private String sFirstName;
    private String sLastName;
    private String sUserName;
    private String sId;

    public ContactClass(String sUserId, String sFirstName, String sLastName, String sUserName) {
        this.sId = sUserId;
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.sUserName = sUserName;
    }

    public String getsFirstName() {
        return sFirstName;
    }

    public String getsLastName() {
        return sLastName;
    }

    public String getsUserName() {
        return sUserName;
    }

    public String getsId() {
        return sId;
    }
}

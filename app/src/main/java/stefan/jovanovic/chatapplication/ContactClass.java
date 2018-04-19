package stefan.jovanovic.chatapplication;

public class ContactClass {
    private String tvFirstName;
    private String tvLastName;
    private String tvUserName;

    public ContactClass(String tvFirstName, String tvLastName, String tvUserName) {
        this.tvFirstName = tvFirstName;
        this.tvLastName = tvLastName;
        this.tvUserName = tvUserName;
    }

    public String gettvFirstName() {
        return tvFirstName;
    }

    public String getTvLastName() {
        return tvLastName;
    }

    public String getTvUserName() {
        return tvUserName;
    }
}

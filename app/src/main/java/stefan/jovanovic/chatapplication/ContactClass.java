package stefan.jovanovic.chatapplication;

import android.graphics.drawable.Drawable;

public class ContactClass {
    private String tvName;

    public ContactClass(String tvName) {
        this.tvName = tvName;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

}

package stefan.jovanovic.chatapplication;

public class Crypto {
    public native String crypt(String message);

    static {
        System.loadLibrary("crypto");
    }
}

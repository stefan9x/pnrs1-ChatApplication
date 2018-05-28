// INotificationBinder.aidl
package stefan.jovanovic.chatapplication;

// Declare any non-default types here with import statements
import stefan.jovanovic.chatapplication.INotificationCallback;

interface INotificationBinder {
    void setCallback(in INotificationCallback callback);
}

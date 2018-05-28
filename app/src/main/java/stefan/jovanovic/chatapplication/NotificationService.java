package stefan.jovanovic.chatapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {

    private NotificationBinder notificationBinder = null;

    @Override
    public IBinder onBind(Intent intent) {

        if (notificationBinder == null) {
            notificationBinder = new NotificationBinder();
        }
        return notificationBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        notificationBinder.stop();
        return super.onUnbind(intent);
    }
}

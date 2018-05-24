package stefan.jovanovic.chatapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.json.JSONException;

import java.io.IOException;


public class NotificationService extends Service {

    private HttpHelper httphelper;
    private Handler handler;

    private static final long PERIOD = 5000L;

    private RunnableExample mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        mRunnable = new RunnableExample();
        mRunnable.start();

        httphelper = new HttpHelper();
        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunnable.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class RunnableExample implements Runnable {
        private Handler mHandler;
        private boolean mRun = false;

        public RunnableExample() {
            mHandler = new Handler(getMainLooper());
        }

        public void start() {
            mRun = true;
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }

			try {
				final boolean response = httphelper.getNotification(getApplicationContext());

				handler.post(new Runnable(){
					public void run() {
						if (response) {
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
									.setSmallIcon(R.drawable.app_icon)
									.setContentTitle("Naslov notifikacije")
									.setContentText("Imate novu poruku")
									.setPriority(NotificationCompat.PRIORITY_DEFAULT);

							NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

						// notificationId is a unique int for each notification that you must define
							notificationManager.notify(2, mBuilder.build());
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
                
           

            mHandler.postDelayed(this, PERIOD);
        }
    }

}
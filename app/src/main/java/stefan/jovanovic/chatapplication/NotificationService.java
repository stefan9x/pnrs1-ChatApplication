package stefan.jovanovic.chatapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.ic_stat_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.ic_launcher))
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getText(R.string.have_new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = httphelper.getNotification(getApplicationContext());

                        handler.post(new Runnable() {
                            public void run() {
                                if (response) {
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
                }
            }).start();

            mHandler.postDelayed(this, PERIOD);
        }
    }

}
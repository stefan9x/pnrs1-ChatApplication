package stefan.jovanovic.chatapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;


public class NotificationBinder extends INotificationBinder.Stub {

    private INotificationCallback mCallback;
    private CallbackCaller mCaller;

    @Override
    public void setCallback(INotificationCallback callback) {
        mCallback = callback;
        mCaller = new CallbackCaller();
        mCaller.start();
    }

    public void stop() {
        mCaller.stop();
    }

    private class CallbackCaller implements Runnable {

        private static final long PERIOD = 5000L;

        private Handler mHandler = null;
        private boolean mRun = true;

        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
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
                mCallback.onCallbackCall();
            } catch (NullPointerException e) {
                // callback is null, do nothing
            } catch (RemoteException e) {
            }

            mHandler.postDelayed(this, PERIOD);
        }
    }
}

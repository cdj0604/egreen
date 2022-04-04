package com.egreen2.egeen2;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";

    private boolean isStop;
    private int count;
    BackgroundAIDL.Stub binder = new BackgroundAIDL.Stub() {
        //각 액티비티로
        @Override
        public int getCount() throws RemoteException {
            return count;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };


    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //백그라운드에 진입하게 되면, 각 액티비티에서 호출, 아래 쓰레드가 실행됨
        Thread counter = new Thread(new quitTimer());
        counter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isStop = true;

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isStop = true;
    }

    private class quitTimer implements Runnable {
        private final Handler handler = new Handler();

        @Override
        public void run() {
            for (count = 0; count < 1200; count++) {
                if (isStop) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                    Log.i(TAG, "백그라운드: " + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }
    }
}

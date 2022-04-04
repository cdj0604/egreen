package com.egreen2.egeen2;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkStateCheck {
    Activity activity;


    public NetworkStateCheck(Activity activity) {
        this.activity = activity;
    }

    public boolean isConnectionNet() {
        boolean isConnection = false;

        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;

        //액티비티 이름을 가져온다.
//        final String activityName = activity.getLocalClassName();

        if (connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isConnected()) {
            //인터넷에 연결된 상태
            Log.i("Net", "네트워크 연결 됨");
            isConnection = true;
        }

        return isConnection;
    }
}

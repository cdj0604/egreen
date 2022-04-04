package com.egreen2.egeen2;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

class NetworkAsyncTasker extends AsyncTask<Void, Void, String> {
    private final String url;
    private final ContentValues values;
    private final String what;
    public AsyncResponse delegate;

    public NetworkAsyncTasker(AsyncResponse delegate, String url, ContentValues values) {
        this.delegate = delegate;
        this.url = url;
        this.values = values;
        this.what = "";
    }

    public NetworkAsyncTasker(AsyncResponse delegate, String url, ContentValues values, String what) {
        this.delegate = delegate;
        this.url = url;
        this.values = values;
        this.what = what;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;

        NetworkConnection nc = new NetworkConnection();
        result = nc.request(url, values);

//        Log.i("Async", "AsyncTask  =====> " + result);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
//            super.onPostExecute(s);
        if (delegate != null) {
            delegate.processFinish(s, what);
        } else {
            Log.i("NAT", "You have not assigned IApiAccessResponse Delegate");
        }
    }

    public interface AsyncResponse {
        void processFinish(String result, String what);
    }
}



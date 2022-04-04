package com.egreen2.egeen2;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

public class PageNetTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = A13_Learning.class.getSimpleName();
    private final String url;
    private final ContentValues values;

    public PageNetTask(A13_Learning a13_learning, String url, ContentValues values) {
        this.url = url;
        this.values = values;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result; //요청 결과를 저장할 변수
        NetworkConnection nc = new NetworkConnection();
        result = nc.request(url, values); // 해당 url로부터 결과물을 얻어온다.

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력

        String result = s;

        Log.i(TAG, "결과 :" + result);

        //DB에 insert하는 일이라 오류 처리만 하면 된다.
        if (result.equals("FAIL")) {
            //네트워크 통신 오류
            Log.i(TAG, "진도처리 통신 오류 발생!!");

        }
    }
}
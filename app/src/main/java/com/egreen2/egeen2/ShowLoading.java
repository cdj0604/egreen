package com.egreen2.egeen2;

import android.app.ProgressDialog;
import android.content.Context;

/*
    [파일명] ShowLoading.java
    [설명] 로딩 화면
    [작성자] 장희원
    [작성일시]
 */

public class ShowLoading {
    Context context;
    String msg;

    /* 로딩을 표시하는 Dialog */
    ProgressDialog loading;

    public ShowLoading(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    public void start() {
        /* 약관을 보여줄 때까지 로딩 Dialog 설정 */
        loading = new ProgressDialog(context);
        loading.setMessage(msg);
        loading.setIndeterminate(true);
        loading.setCancelable(false);

        loading.show();
    }

    public void stop() {
        loading.dismiss();
    }
}

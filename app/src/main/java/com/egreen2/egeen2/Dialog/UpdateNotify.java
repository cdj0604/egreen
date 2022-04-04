package com.egreen2.egeen2.Dialog;


import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.egreen2.egeen2.R;

public class UpdateNotify {
    Dialog dig;
    int layout;
    private final Context context;
    private CsDialogListener csDialogListener;

    public UpdateNotify(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    public void callResultDialog() {
        dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(layout);
        dig.setCancelable(false);
        dig.show();

        String contexts_txt = "" +

                "<h2>업데이트 알림</h2>" +
                "<h5>아래와 같이 업데이트되었습니다.</h5>" +
                //"<h6>1.강의 화면을 세로 또는 가로 모드로 볼 수 있습니다.</h6>" +
                //"<h6>2.이제 모바일에서도 오리엔테이션을 볼 수 있습니다.</h6>" +
                //"<h6>3.멀티태스킹 환경에서 앱이 강제종료 되는 문제를 수정하였습니다.</h6>" +
                //"<h6>4.기타 오류를 수정했습니다.</h6>";
                "<h6>1. 태블릿도 구글 플레이스토어에서 다운로드할 수 있습니다.</h6>" +
                "<h6>2. 메인페이지 베너와 강의콘텐츠 오류 수정하였습니다.</h6>";

        TextView contents = dig.findViewById(R.id.contents);
        contents.setText(Html.fromHtml(contexts_txt));
        contents.setMovementMethod(LinkMovementMethod.getInstance());

        Button btn_ok = dig.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csDialogListener.onPositiveClicked("OK");
                dig.dismiss();
            }
        });
    }

    public void setCsDialogListener(CsDialogListener csDialogListener) {
        this.csDialogListener = csDialogListener;
    }

    public interface CsDialogListener {
        void onPositiveClicked(String s);
    }
}

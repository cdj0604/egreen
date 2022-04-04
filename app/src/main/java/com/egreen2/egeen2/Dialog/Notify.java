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

public class Notify {
    Dialog dig;
    int layout;
    private final Context context;
    private CsDialogListener csDialogListener;

    public Notify(Context context, int layout) {
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
                "<h2>알려드립니다!</h2>" +
                "<div>1.본원은 <font color=#FF0000>1인 1기기</font> 접속 허용하고 았습니다.</div>" +
                "<div>2.모바일에서는 <font color=#FF0000>학습 진도처리와 수강신청만</font> 가능합니다. <font color=#FF0000>시험, 과제, 참여활동은 PC</font>에서 진행해주세요.</div>" +
                "<div>3.<font color=#FF0000>3주차부터 <b>범용공동인증서</b></font>가 필요합니다.</div>" +
                "<div>4.접속환경에 따라 장애가 있을 수 있으며 안정적인 접속환경에서 수강해주세요.</div>" +
                "<div>5.수강 중 문제가 있다면 본원으로 문의해주세요.</div>" +
                "<div>6.자세한 내용은 교육원 홈페이지 참고해주세요.</div>" +
                "<div><a href='http://cb.egreen.co.kr'>홈페이지 바로가기</a></div>";

        TextView contents = dig.findViewById(R.id.contents);
        contents.setText(Html.fromHtml(contexts_txt));
        contents.setMovementMethod(LinkMovementMethod.getInstance());

        Button btn_cancel = dig.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csDialogListener.onPositiveClicked("CANCEL");
                dig.dismiss();
            }
        });

        Button btn_ok = dig.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csDialogListener.onPositiveClicked("OK");
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

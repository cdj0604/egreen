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

public class RegDevice {
    Dialog dig;
    int layout;
    private final Context context;
    private CsDialogListener csDialogListener;

    public RegDevice(Context context, int layout) {
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
                "<h2>새로운 기기 접속</h2>" +
                "<div>새로운 모바일 기기로 접속하셨나요?</div>" +
                "<div>본 원은 1인 1기기 접속을 허용하고 있어요.</div>" +
                "<div>지금 기기로 접속하시려면 <font color=#FF0000>[변경하기]</font>버튼을 눌러주세요.</div>";

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

package com.egreen2.egeen2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.egreen2.egeen2.R;

public class LearningNotify {
    Dialog dig;
    int layout;
    private final Context context;
    private CsDialogListener csDialogListener;

    public LearningNotify(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    public void callResultDialog() {
        dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(layout);
        dig.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dig.getWindow().getAttributes());
        lp.width = 1300;
        lp.height = 1000;

        dig.show();
        Window window = dig.getWindow();
        window.setAttributes(lp);

        String contexts_txt = "" +
                "<h2>모바일 수강 전 한 번 읽어주세요</h2>" +
                "<div>1.학습기간이 지나면 출석이 되지 않습니다.<br><br>" +
                "2.수강 중 전화, 문자, 타 앱 실행 시 출석이 정상적으로 이루어지지 않을 수 있습니다.<br><br>" +
                "2번과 같은 일 발생 시 강의목록(강의선택)화면으로 돌아가신 후 다시 수강하시면 정상적으로 처리됩니다.<br><br>" +
                "감사합니다.</div>";

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

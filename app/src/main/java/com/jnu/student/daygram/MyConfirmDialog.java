package com.jnu.student.daygram;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2018/11/16.
 */

public class MyConfirmDialog extends Dialog{

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private String timeInfo;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public MyConfirmDialog(Context context, String timeInfo) {
        super(context);
        this.context = context;
        this.timeInfo = timeInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        setContentView(view);

        TextView tvConfirm = (TextView) view.findViewById(R.id.dialog_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        TextView tvTimeInfo = (TextView) view.findViewById(R.id.dialog_time);

        tvTimeInfo.setText(timeInfo);


        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.dialog_confirm:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.dialog_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    };
}

package com.jnu.student.daygram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private static final String monthNames[] = {
                "JANUARY", "FEBRUARY", "MARCH",
                "APRIL", "MAY", "JUNE",
                "JULY", "AGUEST", "SEPTEMBER",
                "OCTOBER", "NOVEMBER", "DECEMBER"

    };

    private final String weekNames[] = {"SUNDAY","MONDAY","TUESDAY","WEBNESDAY","THURSDAY","FRIDAY","SATURDAY"};

    DayNote dayNote;
    EditText editArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show);

        Intent intent = getIntent();
        dayNote = (DayNote)intent.getSerializableExtra("dayNote");

        //计算顶部的日期信息
        //星期
        String weekInfo = "";

        Calendar cal = Calendar.getInstance();
        cal.set(dayNote.getYear(), dayNote.getMonth()-1, dayNote.getDay());
        int weekNum = cal.get(Calendar.DAY_OF_WEEK);
        weekInfo += weekNames[weekNum-1];

        String monthYearInfo = "/";
        monthYearInfo += monthNames[dayNote.getMonth()-1];
        monthYearInfo += " ";
        monthYearInfo += dayNote.getDay();
        monthYearInfo += "/";

        monthYearInfo += dayNote.getYear();

        //设置
        TextView weekText = (TextView)findViewById(R.id.detial_week_text);
        if(weekNum == 1) weekText.setTextColor(Color.RED);
        weekText.setText(weekInfo);

        TextView monthYearText = (TextView)findViewById(R.id.detial_month_year_text);
        monthYearText.setText(monthYearInfo);

        //编辑区域
        editArea= (EditText)findViewById(R.id.edit_area);
        editArea.setText(dayNote.getContent());
        editArea.setCursorVisible(false);
        editArea.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    editArea.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });

        //clock按钮
        ImageView clock = (ImageView)findViewById(R.id.time_button);
        clock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                Date date = new Date();
                String LgTime = sdf.format(date);

                String res = LgTime + " ";

                int index = editArea.getSelectionStart();
                Editable editable = editArea.getText();
                editable.insert(index, res);


            }
        });

        //DONE按钮
        TextView doneBtn = (TextView)findViewById(R.id.done_bottom);
        doneBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dayNote.setContent(editArea.getText().toString());
                dayNote.write(getApplicationContext());
                Toast.makeText(DetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        //返回按钮
        TextView backBottom = (TextView)findViewById(R.id.back_bottom);
        backBottom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}



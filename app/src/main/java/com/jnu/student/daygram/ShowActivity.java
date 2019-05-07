package com.jnu.student.daygram;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    public static final int INTENT_CODE = 100;

    public static final String monthNames[] = {
            "JANUARY", "FEBRUARY", "MARCH",
            "APRIL", "MAY", "JUNE",
            "JULY", "AGUEST", "SEPTEMBER",
            "OCTOBER", "NOVEMBER", "DECEMBER"

    };

    private List<DayNote> dayNoteList = new ArrayList<DayNote>();

    private int showYear;
    private int showMonth;
    private int dayNum;
    private int nowYear;
    private int nowMonth;
    DayNoteAdapter adapter;
    MonthBarAdapter monthBarAdapter;
    RecyclerView recyclerView;
    RecyclerView monthBar;
    RecyclerView yearBar;
    TextView showMonthButton;
    TextView showYearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initDayNote();
        recyclerView = (RecyclerView)findViewById(R.id.diary_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DayNoteAdapter(this , dayNoteList, DayNoteAdapter.TYPE.NORMAL);
        recyclerView.setAdapter(adapter);

        //今天
        Calendar cal = Calendar.getInstance();
        nowYear = cal.get(Calendar.YEAR);
        nowMonth = cal.get(Calendar.MONTH) + 1;

        //初始化月份条
        monthBar = (RecyclerView)findViewById(R.id.month_bar);
        LinearLayoutManager monthBarLayoutMg = new LinearLayoutManager(this);
        monthBarLayoutMg.setOrientation(LinearLayoutManager.HORIZONTAL);
        monthBar.setLayoutManager(monthBarLayoutMg);
        monthBarAdapter = new MonthBarAdapter(this, showMonth);
        monthBar.setAdapter(monthBarAdapter);
        monthBar.setVisibility(View.INVISIBLE);

        //初始化年份条
        yearBar = (RecyclerView)findViewById(R.id.year_bar);
        LinearLayoutManager yearBarLayoutMg = new LinearLayoutManager(this);
        yearBarLayoutMg.setOrientation(LinearLayoutManager.HORIZONTAL);
        yearBar.setLayoutManager(yearBarLayoutMg);
        YearBarAdapter yearBarAdapter = new YearBarAdapter(this, showYear);
        yearBar.setAdapter(yearBarAdapter);
        yearBar.setVisibility(View.INVISIBLE);


        //添加按钮的点击事件
        ImageView addDayButton = (ImageView)findViewById(R.id.add_day_bottom);
        addDayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //获取今天的日期，添加今日日记
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;  //需要加1
                int day = cal.get(Calendar.DATE);
                DayNote now = new DayNote();
                if(!now.read(ShowActivity.this, year, month, day)){
                    now = new DayNote(year, month, day, "");
                }
                Intent intent = new Intent(ShowActivity.this, DetailActivity.class);
                intent.putExtra("dayNote", now);
                startActivityForResult(intent, ShowActivity.INTENT_CODE);

            }
        });

        //日记浏览按钮
        ImageView detailShowButton = (ImageView)findViewById(R.id.detail_show_button);
        detailShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getShowType() == DayNoteAdapter.TYPE.DETAIL){
                    refresh(DayNoteAdapter.TYPE.NORMAL);
                } else if(adapter.getShowType() == DayNoteAdapter.TYPE.NORMAL){
                    refresh(DayNoteAdapter.TYPE.DETAIL);
                }

            }
        });

        //月份按钮
        showMonthButton = (TextView)findViewById(R.id.show_month_button);
        showMonthButton.setText(monthNames[showMonth-1]);
        showMonthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monthBar.setVisibility(View.VISIBLE);
            }
        });

        //年份按钮
        showYearButton = (TextView)findViewById(R.id.show_year_button);
        showYearButton.setText(String.format("%d",showYear));
        showYearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                yearBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initDayNote(){
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH) + 1;  //需要加1
//        int day = cal.get(Calendar.DATE)+4;
//        String content = "今天在打码";
//        DayNote now = new DayNote(year, month, day, content);
//        now.write(ShowActivity.this);
//        dayNoteList.add(now);
//
//        cal.set(2016, 1, 12);  //nice
//        int dayNum = cal.getActualMaximum(Calendar.DATE);
//        Log.e("temp", "initDayNote: "+dayNum );

        readAMonth(2018,11);

    }

    private void readAMonth(int year, int month){
        Calendar cal = Calendar.getInstance();

        //获取今天的日子，用来防止显示未来
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;  //需要加1
        int nowDay = cal.get(Calendar.DATE);

        this.showYear = year;
        this.showMonth = month;
        cal.set(year, month-1, 1);
        if(year == nowYear && month == nowMonth){
            dayNum = nowDay;
        } else {
            dayNum = cal.getActualMaximum(Calendar.DATE);
        }

        dayNoteList.clear();
        for(int i=0; i<dayNum; i++){
            DayNote dayNote = new DayNote();
            if(dayNote.read(ShowActivity.this, year, month, i+1)){
                dayNoteList.add(dayNote);
            } else {
                dayNoteList.add(dayNote);

            }
        }
    }

    public void refresh(DayNoteAdapter.TYPE showType){
        //重新刷新整个月的日记
        readAMonth(showYear, showMonth);
        adapter = new DayNoteAdapter(this , dayNoteList, showType);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ShowActivity.INTENT_CODE:
                refresh(adapter.getShowType());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getShowYear() {
        return showYear;
    }

    public void setShowYear(int showYear) {
        this.showYear = showYear;
    }

    public int getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(int showMonth) {
        this.showMonth = showMonth;
    }

    public int getNowYear() {
        return nowYear;
    }

    public int getNowMonth() {
        return nowMonth;
    }

}

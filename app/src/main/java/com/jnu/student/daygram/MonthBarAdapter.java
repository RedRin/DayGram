package com.jnu.student.daygram;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2018/11/15.
 */

public class MonthBarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int selectedMonth;
    ShowActivity showActivity;

    final String monthNames[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

    static class MonthHolder extends RecyclerView.ViewHolder{
        View monthView;
        TextView monthItem;
        public MonthHolder(View view){
            super(view);
            monthView = view;
            monthItem = (TextView)view.findViewById(R.id.month_bar_item);
        }
    }

    public MonthBarAdapter(ShowActivity showActivity ,int selectedMonth){
        this.selectedMonth = selectedMonth;
        this.showActivity = showActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_item, parent, false);
        final MonthHolder holder = new MonthHolder(view);

        holder.monthView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if(showActivity.getShowYear() == showActivity.getNowYear() && position+1>showActivity.getNowMonth()){

                } else {
                    showActivity.setShowMonth(position+1);
                    showActivity.refresh(showActivity.adapter.getShowType());
                    selectedMonth = position+1;
                    notifyDataSetChanged();
                    showActivity.showMonthButton.setText(ShowActivity.monthNames[position]);
                    showActivity.monthBar.setVisibility(View.INVISIBLE);

                }

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MonthHolder monthHolder = (MonthHolder)holder;
        monthHolder.monthItem.setText(monthNames[position]);

        if(showActivity.getShowYear() == showActivity.getNowYear()){
            if(position + 1 == selectedMonth){
                monthHolder.monthItem.setBackgroundResource(R.drawable.selected);
            } else if(position + 1 > showActivity.getNowMonth()){
                monthHolder.monthItem.setBackgroundResource(R.drawable.disable);
            }
            else {
                monthHolder.monthItem.setBackgroundResource(R.drawable.no_note);
            }
        } else {
            if(position + 1 == selectedMonth){
                monthHolder.monthItem.setBackgroundResource(R.drawable.selected);
            } else {
                monthHolder.monthItem.setBackgroundResource(R.drawable.no_note);
            }
        }


    }

    @Override
    public int getItemCount() {
        return 12;//12个月
    }
}

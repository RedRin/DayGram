package com.jnu.student.daygram;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/11/16.
 */

public class YearBarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int selectedYear;
    ShowActivity showActivity;
    private int startYear;
    private static final int YEARNUM = 10;

    static class YearHolder extends RecyclerView.ViewHolder{
        View yearView;
        TextView yearItem;
        public YearHolder(View view){
            super(view);
            yearView = view;
            yearItem = (TextView)view.findViewById(R.id.year_bar_item);
        }
    }

    public YearBarAdapter(ShowActivity showActivity, int selectedYear){
        this.showActivity = showActivity;
        this.selectedYear = selectedYear;
        startYear = showActivity.getNowYear() - YEARNUM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_item, parent, false);
        final YearHolder holder = new YearHolder(view);

        holder.yearView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                showActivity.setShowYear(startYear + position);
                showActivity.refresh(showActivity.adapter.getShowType());
                selectedYear = startYear + position;
                notifyDataSetChanged();
                showActivity.monthBarAdapter.notifyDataSetChanged();
                showActivity.showYearButton.setText(String.format("%d",selectedYear));
                showActivity.yearBar.setVisibility(View.INVISIBLE);

            }

        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YearHolder yearHolder = (YearHolder)holder;
        yearHolder.yearItem.setText(String.format("%d",startYear + position));

        if(startYear + position == selectedYear){
            yearHolder.yearItem.setTextColor(Color.BLACK);
        } else {
            yearHolder.yearItem.setTextColor(Color.rgb(128,128,128));
        }
    }

    @Override
    public int getItemCount() {
        return YEARNUM + 1;
    }
}

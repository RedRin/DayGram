package com.jnu.student.daygram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2018/11/10.
 */

public class DayNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DayNote> mDayNoteList;
    final String weekNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    private ShowActivity showActivity;
    private TYPE showType = TYPE.NORMAL;

    public TYPE getShowType(){
        return showType;
    }

    public static enum TYPE{
        NORMAL,
        DETAIL
    }

    public static enum ITEM_TYPE{
        ITEM_TYPE_FILL,
        ITEM_TYPE_EMPTY
    }

    static class FillHolder extends RecyclerView.ViewHolder{
        View dayView;
        TextView week;
        TextView day;
        TextView content;
        public FillHolder(View view){
            super(view);
            dayView = view;
            week = (TextView)view.findViewById(R.id.week_text);
            day = (TextView)view.findViewById(R.id.day_text);
            content = (TextView)view.findViewById(R.id.content_text);
        }
    }

    static class EmptyHolder extends  RecyclerView.ViewHolder{
        ImageView noNote;
        public EmptyHolder(View view){
            super(view);
            noNote = (ImageView)view.findViewById(R.id.no_note);
        }
    }

    static class NullHolder extends RecyclerView.ViewHolder{
        public NullHolder(View view){
            super(view);
        }
    }

    public DayNoteAdapter(ShowActivity showActivity ,List<DayNote> dayNoteList, TYPE showType){
        this.showActivity = showActivity;
        this.mDayNoteList = dayNoteList;
        this.showType = showType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(showType == TYPE.NORMAL){ //正常显示
            if(viewType == ITEM_TYPE.ITEM_TYPE_EMPTY.ordinal()){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_day_item, parent, false);
                final EmptyHolder holder = new EmptyHolder(view);

                holder.noNote.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        DayNote dayNote = new DayNote(showActivity.getShowYear(), showActivity.getShowMonth(), position+1, "");
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("dayNote", dayNote);
                        showActivity.startActivityForResult(intent, ShowActivity.INTENT_CODE);
                    }
                });

                return holder;
            } else if(viewType == ITEM_TYPE.ITEM_TYPE_FILL.ordinal()){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
                final FillHolder holder = new FillHolder(view);

                //每日方框的点击事件，调用编辑界面
                holder.dayView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        DayNote dayNote = mDayNoteList.get(position);
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("dayNote", dayNote);
                        showActivity.startActivityForResult(intent, ShowActivity.INTENT_CODE);
                    }
                });
                //长按事件,删除事件
                holder.dayView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getAdapterPosition();
                        final DayNote dayNote = mDayNoteList.get(position);
                        String info = " "+dayNote.getYear()+"-"+dayNote.getMonth()+"-"+dayNote.getDay()+" ";

                        final MyConfirmDialog dialog = new MyConfirmDialog(showActivity, "是否删除"+info+"的日记？");
                        dialog.show();
                        dialog.setClicklistener(new MyConfirmDialog.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                dayNote.delete(showActivity);
                                showActivity.refresh(showActivity.adapter.getShowType());
                            }

                            @Override
                            public void doCancel() {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        return true;
                    }
                });

                return holder;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
                final EmptyHolder holder = new EmptyHolder(view);
                return holder;
            }
        } else { //详细显示
            if(viewType == ITEM_TYPE.ITEM_TYPE_FILL.ordinal()){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_detail_item, parent, false);
                final FillHolder holder = new FillHolder(view);

                //每日方框的点击事件，调用编辑界面
                holder.dayView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        DayNote dayNote = mDayNoteList.get(position);
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("dayNote", dayNote);
                        showActivity.startActivityForResult(intent, ShowActivity.INTENT_CODE);
                    }
                });

                //长按事件,删除事件
                holder.dayView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getAdapterPosition();
                        final DayNote dayNote = mDayNoteList.get(position);
                        String info = " "+dayNote.getYear()+"-"+dayNote.getMonth()+"-"+dayNote.getDay()+" ";

                        final MyConfirmDialog dialog = new MyConfirmDialog(showActivity, "是否删除"+info+"的日记？");
                        dialog.show();
                        dialog.setClicklistener(new MyConfirmDialog.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                dayNote.delete(showActivity);
                                showActivity.refresh(showActivity.adapter.getShowType());
                            }

                            @Override
                            public void doCancel() {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                        return true;
                    }
                });

                return holder;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.null_item, parent, false);
                final NullHolder holder = new NullHolder(view);
                return holder;
            }
        }





    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  FillHolder){
            FillHolder fillHolder = (FillHolder)holder;
            DayNote dayNote = mDayNoteList.get(position);
            Calendar cal = Calendar.getInstance();
            cal.set(dayNote.getYear(), dayNote.getMonth()-1, dayNote.getDay());
            int weekNum = cal.get(Calendar.DAY_OF_WEEK);

            if(weekNum == 1) fillHolder.week.setTextColor(Color.RED);
            else fillHolder.week.setTextColor(Color.BLACK);
            if(showType == TYPE.NORMAL)
                fillHolder.week.setText(weekNames[weekNum-1]);
            else
                fillHolder.week.setText(weekNames[weekNum-1]+" /");
            fillHolder.day.setText(Integer.toString(dayNote.getDay()));
            fillHolder.content.setText(dayNote.getContent());
        } else if(holder instanceof EmptyHolder){
            ///
            EmptyHolder emptyHolder = (EmptyHolder)holder;
            Calendar cal = Calendar.getInstance();
            cal.set(showActivity.getShowYear(), showActivity.getShowMonth()-1, position+1);
            int weekNum = cal.get(Calendar.DAY_OF_WEEK);
            if(weekNum == 1) emptyHolder.noNote.setImageResource(R.drawable.no_note_red);
            else emptyHolder.noNote.setImageResource(R.drawable.no_note);
        }

    }

    @Override
    public int getItemCount() {
        return mDayNoteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        DayNote dayNote = mDayNoteList.get(position);
        if(dayNote.isEmpty()){
            return ITEM_TYPE.ITEM_TYPE_EMPTY.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_FILL.ordinal();
        }

    }
}

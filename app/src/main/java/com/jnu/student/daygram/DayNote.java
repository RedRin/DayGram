package com.jnu.student.daygram;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by lenovo on 2018/11/10.
 */

public class DayNote implements Serializable{
    private String content;
    private int year;
    private int month;
    private int day;
    private boolean isEmpty;
    

    public DayNote(int year, int month, int day, String content){
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.isEmpty = false;
    }

    public DayNote(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.content = "";
        this.isEmpty = true;
    }

    public DayNote(Context context, int year, int month, int day){
        read(context, year, month, day);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public boolean isEmpty(){
        return this.isEmpty;
    }

    public boolean write(Context context){
        String dirsrc = context.getFilesDir() + "/" + year + "/" + month;
        File dir = new File(dirsrc);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String src = context.getFilesDir() + "/" + year + "/" + month + "/" + day;
        File outputFile = new File(src);


        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            Log.e("DayNote", "write: 创建文件失败");
            return false;
        }
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);  //写入到文件
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("DayNote", "write: 文件不存在");
            return false;
        } catch (IOException e) {
            Log.e("DayNote", "write: 输入输出错误");
            return false;
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e("DayNote", "write: 关闭fos失败");
                }
            }
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    Log.e("DayNote", "write: 关闭oos失败");
                }
            }
        }

    }

    public boolean read(Context context, int year, int month, int day){
        String src = context.getFilesDir() + "/" + year + "/" + month + "/" + day;
        File inputFile = new File(src);
        if(!inputFile.exists()) return false;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(inputFile);
            ois = new ObjectInputStream(fis);
            DayNote temp =  (DayNote)ois.readObject();
            this.year = temp.year;
            this.month = temp.month;
            this.day = temp.day;
            this.content = temp.content;
            this.isEmpty = temp.isEmpty;
            return true;

        } catch (FileNotFoundException e) {
            Log.e("DayNote", "read: 文件未找到");
            return false;
        } catch (IOException e) {
            Log.e("DayNote", "read: 输入输出错误");
            return false;
        } catch (ClassNotFoundException e) {
            Log.e("DayNote", "read: 类没有找到");
            return false;
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("DayNote", "read: 关闭fis失败");
                }
            }
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    Log.e("DayNote", "read: 关闭ois失败");
                }
            }
        }
    }

    public boolean delete(Context context){
        String src = context.getFilesDir() + "/" + year + "/" + month + "/" + day;
        File openFile = new File(src);
        if(openFile.exists()) {
            openFile.delete();
            return true;
        }
        return false;
    }
}

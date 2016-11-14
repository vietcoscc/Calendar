package com.t3h.item;

import java.io.Serializable;

/**
 * Created by vaio on 11/13/2016.
 */

public class EventCalendar implements Serializable{
    private String date;
    private String noteName;
    private String noteContent;
    private String time;
    private int mode;
    private int warningLevels;
    private int looping;

    public EventCalendar(String date, String noteName, String noteContent, String time, int mode, int warningLevels, int looping) {
        this.date = date;
        this.noteName = noteName;
        this.noteContent = noteContent;
        this.time = time;
        this.mode = mode;
        this.warningLevels = warningLevels;
        this.looping = looping;
    }

    @Override
    public String toString() {
        return date+"_"+noteName+"_"+noteContent+"_"+time+"_"+mode+"_"+warningLevels+"_"+looping;
    }
    public int getDay(){
        return Integer.parseInt(date.split("/")[0]);
    }
    public int getMonth(){
        return Integer.parseInt(date.split("/")[1]);
    }
    public int getYear(){
        return Integer.parseInt(date.split("/")[2]);
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getWarningLevels() {
        return warningLevels;
    }

    public void setWarningLevels(int warningLevels) {
        this.warningLevels = warningLevels;
    }

    public int getLooping() {
        return looping;
    }

    public void setLooping(int looping) {
        this.looping = looping;
    }
}

package com.spsoft.mynote;

import com.google.firebase.Timestamp;

public class Notemodel {
    String title;
    String note;
    Timestamp time;
    String address;

    Notemodel(){}

    public Notemodel(String note, String title, Timestamp time, String address) {
        this.note = note;
        this.title = title;
        this.time = time;
        this.address = address;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Notemodel{" +
                "note='" + note + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", address=" + address +
                '}';
    }


}



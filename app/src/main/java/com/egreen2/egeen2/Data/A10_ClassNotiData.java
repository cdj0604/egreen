package com.egreen2.egeen2.Data;

import java.io.Serializable;

public class A10_ClassNotiData implements Serializable {
    int notiId;
    String title;
    String regDate;

    public A10_ClassNotiData(int notiId, String title, String regDate) {
        this.notiId = notiId;
        this.title = title;
        this.regDate = regDate;
    }

    public int getNotiId() {
        return notiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}

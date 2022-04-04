package com.egreen2.egeen2.Data;

public class A05_NoticeListData {
    int noticeimportance;
    String noticeCid;
    String noticeTitle;
    String noticeDate;

    public A05_NoticeListData(int noticeimportance, String noticeCid, String noticeTitle, String noticeDate) {
        this.noticeimportance = noticeimportance;
        this.noticeCid = noticeCid;
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
    }

    public int getNoticeimportance() {
        return noticeimportance;
    }

    public String getNoticeCid() {
        return noticeCid;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeDate() {
        return noticeDate;
    }
}


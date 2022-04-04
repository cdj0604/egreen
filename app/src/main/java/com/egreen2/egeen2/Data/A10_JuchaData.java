package com.egreen2.egeen2.Data;

public class A10_JuchaData {
    String sCid, eCid, fileRoot;
    String title, studyDt;
    int jucha, chasi;
    String watchedTime, fullTime;
    String readPage, fullPage;
    String totalRatio;
    boolean gu;
    boolean enable;

    public A10_JuchaData(String sCid, String eCid, String fileRoot, String title, String studyDt,
                         int jucha, int chasi, String watchedTime, String fullTime,
                         String readPage, String fullPage, String totalRatio, boolean gu, boolean enable) {
        this.sCid = sCid;
        this.eCid = eCid;
        this.fileRoot = fileRoot;
        this.title = title;
        this.studyDt = studyDt;
        this.jucha = jucha;
        this.chasi = chasi;
        this.watchedTime = watchedTime;
        this.fullTime = fullTime;
        this.readPage = readPage;
        this.fullPage = fullPage;
        this.totalRatio = totalRatio;
        this.gu = gu;
        this.enable = enable;
    }

    public String getsCid() {
        return sCid;
    }

    public String geteCid() {
        return eCid;
    }

    public String getFileRoot() {
        return fileRoot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudyDt() {
        return studyDt;
    }

    public int getJucha() {
        return jucha;
    }

    public void setJucha(int jucha) {
        this.jucha = jucha;
    }

    public int getChasi() {
        return chasi;
    }

    public String getWatchedTime() {
        return watchedTime;
    }

    public String getFullTime() {
        return fullTime;
    }

    public String getReadPage() {
        return readPage;
    }

    public String getFullPage() {
        return fullPage;
    }

    public String getTotalRatio() {
        return totalRatio;
    }

    public boolean isGu() {
        return gu;
    }

    public boolean isEnable() {
        return enable;
    }
}

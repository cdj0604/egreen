package com.egreen2.egeen2.Data;

public class A08_CurriculumListData {
    String classTitle;
    String classId;
    String directoryName;

    public A08_CurriculumListData(String classTitle, String classId, String directoryName) {
        this.classTitle = classTitle;
        this.classId = classId;
        this.directoryName = directoryName;
    }

    public String getclassTitle() {
        return classTitle;
    }

    public String getclassId() {
        return classId;
    }

    public String getdirectoryName() {
        return directoryName;
    }
}
package com.egreen2.egeen2.Data;

public class A09_MyClassListData {
    boolean isEnable;
    String classId;
    String classTitle;
    String studyDate;
    String attendRatio;
    String isReadOrientation;
    String isSurvey;
    String isThereHighSchoolName;
    String isDongPost;
    String directoryName;
    String myGoal;
    String myNote;
    String selfInt;
    String discussion;

    public A09_MyClassListData(boolean isEnable, String classId, String classTitle, String studyDate, String attendRatio, String isReadOrientation,
                               String isSurvey, String isThereHighSchoolName, String isDongPost, String directoryName,
                               String myGoal, String myNote, String selfInt, String discussion) {
        this.isEnable = isEnable;
        this.classId = classId;
        this.classTitle = classTitle;
        this.studyDate = studyDate;
        this.attendRatio = attendRatio;
        this.isReadOrientation = isReadOrientation;
        this.isSurvey = isSurvey;
        this.isThereHighSchoolName = isThereHighSchoolName;
        this.isDongPost = isDongPost;
        this.directoryName = directoryName;
        this.myGoal = myGoal;
        this.myNote = myNote;
        this.selfInt = selfInt;
        this.discussion = discussion;
    }

    public String getClassId() {
        return classId;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public String getAttendRatio() {
        return attendRatio;
    }

    public String getIsReadOrientation() {
        return isReadOrientation;
    }

    public String getIsSurvey() {
        return isSurvey;
    }

    public String getIsThereHighSchoolName() {
        return isThereHighSchoolName;
    }

    public String isDongPost() {
        return isDongPost;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getMyGoal() {
        return myGoal;
    }

    public String getMyNote() {
        return myNote;
    }

    public String getSelfInt() {
        return selfInt;
    }

    public String getDiscussion() {
        return discussion;
    }
}

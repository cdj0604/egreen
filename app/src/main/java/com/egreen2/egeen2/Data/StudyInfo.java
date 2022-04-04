package com.egreen2.egeen2.Data;

import java.io.Serializable;

public class StudyInfo implements Serializable {
    String userId, classId, loginNumber;
    String directoryName, userName;
    String studyDate, classTitle, dongPost, myGoal, myNote, selfIntro, orientation, discussion;
    String readPage, fullPage;

    public StudyInfo() {
        this.userId = "";
        this.classId = "";
        this.loginNumber = "";
        this.directoryName = "";
        this.userName = "";
        this.studyDate = "";
        this.classTitle = "";
        this.dongPost = "";
        this.myGoal = "";
        this.myNote = "";
        this.selfIntro = "";
        this.orientation = "";
        this.discussion = "";
        this.readPage = "";
        this.fullPage = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getLoginNumber() {
        return loginNumber;
    }

    public void setLoginNumber(String loginNumber) {
        this.loginNumber = loginNumber;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getDongPost() {
        return dongPost;
    }

    public void setDongPost(String dongPost) {
        this.dongPost = dongPost;
    }

    public String getMyGoal() {
        return myGoal;
    }

    public void setMyGoal(String myGoal) {
        this.myGoal = myGoal;
    }

    public String getMyNote() {
        return myNote;
    }

    public void setMyNote(String myNote) {
        this.myNote = myNote;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }
}

package com.example.fsy.manager_date;


public class GoalData {
    private int ID;
    private String name;
    private String startTime;
    private String endTime;
    private String alertTime;
    private String note;
    private int importance;
    private int type;
    private int father;
    private int sonNumber;
    private String userName;
    private int completed;

    public GoalData() {
    }

    public GoalData(int ID, String name, String startTime, String endTime, String alertTime, String note, int importance, int type, int father, int sonNumber, String userName, int completed) {
        super();
        this.ID = ID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alertTime = alertTime;
        this.note = note;
        this.importance = importance;
        this.type = type;
        this.father = father;
        this.sonNumber = sonNumber;
        this.userName = userName;
        this.completed = completed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFather() {
        return father;
    }

    public void setFather(int father) {
        this.father = father;
    }

    public int getSonNumber() {
        return sonNumber;
    }

    public void setSonNumber(int sonNumber) {
        this.sonNumber = sonNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}



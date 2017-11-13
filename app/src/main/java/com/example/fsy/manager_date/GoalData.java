package com.example.fsy.manager_date;

/**
 * Created by fsy on 2017/11/5.
 */

public class GoalData {
    private String goalName;
    private int   goalNumber;
    private String smallGoal;
    private String goalDescription;
    private String endTime;

    public String getGoalName() {             //获取用户名
        return goalName;
    }
    public void setGoalName(String goalName){this.goalName=goalName;}

    public int getGoalNumber()
    {
        return goalNumber;
    }
    public  void setGoalNumber(int goalNumber)
    {
        this.goalNumber=goalNumber;
    }

    public String getSmallGoal()
    {
        return smallGoal;
    }
    public void setSmallGoal(String smallGoal)
    {
        this.smallGoal=smallGoal;
    }

    public String getGoalDescription()
    {
        return goalDescription;
    }
    public void setGoalDescription(String goalDescription)
    {
        this.goalDescription=goalDescription;
    }

    public String getEndTime(){return endTime;}
    public void setEndTime(String endTime) {this.endTime=endTime;}

    public GoalData(String goalName, int  goalNumber,String smallGoal,String goalDescription,String endTime) {
        super();
        this.goalName = goalName;
        this.goalNumber = goalNumber;
        this.smallGoal=smallGoal;
        this.goalDescription=goalDescription;
        this.endTime=endTime;
    }

}



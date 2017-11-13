package com.example.fsy.manager_date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class GoalDataManager {
    private static final String DB_NAME = "goal_data";
    private static final String TABLE_NAME = "goals";
    private static final String ID = "_id";
    private static final String GOAL_NAME = "goal_name";
    private static final String GOAL_NUMBER = "goal_number";
    private static final String SMALL_GOAL ="small_goal";
    private static final String GOAL_DESCRIPTION="goal_description";
    private static final String END_TIME="end_time";
    private static final int DB_VERSION =5;
    private static final String USER_NAME = "user_name";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    private String user;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID + " integer primary key," + GOAL_NAME + " varchar,"
                + GOAL_NUMBER + " integer," +  SMALL_GOAL + " varchar,"+
                GOAL_DESCRIPTION + " varchar,"+ END_TIME + " varchar," +
                USER_NAME + " varchar" + ");";

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    // 构造函数
    private Context mContext = null;
    public GoalDataManager(Context context, String user) {
        this.user = user;
        mContext = context;
    }

    // 打开数据看
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    //添加新项目
    public long insertGoalData(GoalData goalData) {
        String goalName=goalData.getGoalName();
        int goalNumber=goalData.getGoalNumber();
        String smallGoal=goalData.getSmallGoal();
        String goalDecription=goalData.getGoalDescription();
        String endTime= goalData.getEndTime();
        ContentValues values = new ContentValues();
        values.put(GOAL_NAME, goalName);
        values.put(GOAL_NUMBER, goalNumber);
        values.put(SMALL_GOAL, smallGoal);
        values.put(GOAL_DESCRIPTION, goalDecription);
        values.put(END_TIME, endTime);
        values.put(USER_NAME, this.user);
        Log.e("BUG",this.user);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    //更新项目信息，如修改密码
   /* public boolean updateGoalData(GoalData goalData) {
        //int id = goalData.getGoalId();
        String goalName = goalData.getGoalName();
        String goalPwd = goalData.getGoalPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, goalName);
        values.put(USER_PWD, goalPwd);
        return mSQLiteDatabase.update(TABLE_NAME, values,null, null) > 0;
        //return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }*/

    // 根据id返回行
    public Cursor fetchGoalData(int id) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, ID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // 返回所有行
    public Cursor fetchAllGoalDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
    }

    // 返回所有目标的某一列
    public String[] getAllGoalsByColumn(String column)
    {
        if(mSQLiteDatabase==null) return null;
        @SuppressLint("Recycle") Cursor cursor = mSQLiteDatabase.query(TABLE_NAME, new String[]{column}, null,null,null,null,null);
        ArrayList<String> ret = new ArrayList<String>();
        while (cursor.moveToNext())
            ret.add(cursor.getString(0));
        return ret.toArray(new String[0]);
    }

    //根据目标名注销
    public boolean deleteGoalDatabyname(String name) {
        return mSQLiteDatabase.delete(TABLE_NAME, GOAL_NAME + "=" + name, null) > 0;
    }

    //删除所有目标
    public boolean deleteAllGoalDatas() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    // 根据id查询列的值
    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchGoalData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }

    // 根据id更新列的值
    public boolean updateGoalDataById(String columnName, int id, String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    // 根据目标名找目标，可以判断注册时目标名是否已经存在
    public int findGoalByName(String goalName){
        int result=0;
        String []temp=new String[10];
        temp[0]=goalName;
        Cursor mCursor=mSQLiteDatabase.rawQuery("select * from goals where GOAL_NAME like ?",temp);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
        }
        return result;
    }



}

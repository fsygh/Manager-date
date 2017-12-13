package com.example.fsy.manager_date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.Goal;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;


public class GoalDataManager {
    private static final String DB_NAME = "goal_data";
    private static final String TABLE_NAME = "goals";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String ALERT_TIME = "alert_time";
    private static final String NOTE = "note";
    private static final String IMPORTANCE = "importance";
    private static final String TYPE = "type";
    private static final String FATHER = "father";
    private static final String SON_NUMBER = "son_number";
    private static final String USER_NAME = "user_name";
    private static final String COMPLETED = "completed";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase GoalDatabaseOperator = null;
    private DatabaseHelper GoalDatabaseHelper = null;
    private Context GoalContext = null;

    private String user;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID + " integer primary key," + NAME + " varchar,"
                + START_TIME + " datetime," + END_TIME + " datetime,"
                + ALERT_TIME + " datetime," + NOTE + " varchar,"
                + IMPORTANCE + " integer," + TYPE + " integer," +
                FATHER + " integer," + SON_NUMBER + " integer," +
                USER_NAME + " varchar," + COMPLETED + " integer" + ");";

        DatabaseHelper(Context context) {
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

    public GoalDataManager(Context context, String user) {
        this.user = user;
        GoalContext = context;
    }

    // 打开数据库
    public void openGoalDatabase() throws SQLException {
        GoalDatabaseHelper = new DatabaseHelper(GoalContext);
        GoalDatabaseOperator = GoalDatabaseHelper.getWritableDatabase();
    }

    // 关闭数据库
    public void closeGoalDatabase() throws SQLException {
        GoalDatabaseHelper.close();
    }

    // 添加新目标
    public void insertGoalData(GoalData goalData) {
        ContentValues values = new ContentValues();
        values.put(NAME, goalData.getName());
        values.put(START_TIME, goalData.getStartTime());
        values.put(END_TIME, goalData.getEndTime());
        values.put(ALERT_TIME, goalData.getAlertTime());
        values.put(NOTE, goalData.getNote());
        values.put(IMPORTANCE, goalData.getImportance());
        values.put(TYPE, goalData.getType());
        values.put(FATHER, goalData.getFather());
        values.put(SON_NUMBER, goalData.getSonNumber());
        values.put(USER_NAME, goalData.getUserName());
        values.put(COMPLETED, goalData.getCompleted());
        GoalDatabaseOperator.insert(TABLE_NAME, ID, values);
        if (goalData.getFather() != 0) {
            GoalData father = fetchGoalDatasByID(goalData.getFather());
            father.setSonNumber(father.getSonNumber() + 1);
            updateGoalData(father);
        }
    }

    // 更新目标信息
    public boolean updateGoalData(GoalData goalData) {
        ContentValues values = new ContentValues();
        values.put(NAME, goalData.getName());
        values.put(START_TIME, goalData.getStartTime());
        values.put(END_TIME, goalData.getEndTime());
        values.put(ALERT_TIME, goalData.getAlertTime());
        values.put(NOTE, goalData.getNote());
        values.put(IMPORTANCE, goalData.getImportance());
        values.put(TYPE, goalData.getType());
        values.put(FATHER, goalData.getFather());
        values.put(SON_NUMBER, goalData.getSonNumber());
        values.put(USER_NAME, goalData.getUserName());
        values.put(COMPLETED, goalData.getCompleted());
        return GoalDatabaseOperator.update(TABLE_NAME, values, ID + "=" + goalData.getID(), null) > 0;
    }

    // 把cursor转化为GoalData
    private GoalData cursor2GoalData(Cursor cursor) {
        GoalData ret = new GoalData();
        ret.setID(cursor.getInt(0));
        ret.setName(cursor.getString(1));
        ret.setStartTime(cursor.getString(2));
        ret.setEndTime(cursor.getString(3));
        ret.setAlertTime(cursor.getString(4));
        ret.setNote(cursor.getString(5));
        ret.setImportance(cursor.getInt(6));
        ret.setType(cursor.getInt(7));
        ret.setFather(cursor.getInt(8));
        ret.setSonNumber(cursor.getInt(9));
        ret.setUserName(cursor.getString(10));
        ret.setCompleted(cursor.getInt(11));
        return ret;
    }

    // 返回某目标的所有子目标
    public ArrayList<GoalData> fetchAllSubGoalDatas(int id) {
        Cursor cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, FATHER + "=" +
                id, null, null, null, null, null);
        ArrayList<GoalData> ret = new ArrayList<>();
        while (cursor.moveToNext())
            ret.add(cursor2GoalData(cursor));
        return ret;
    }

    // 根据ID返回目标
    public GoalData fetchGoalDatasByID(int id) {
        Cursor cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, ID + "=" + id,
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor2GoalData(cursor);
    }

    // 根据条件返回一个目标
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GoalData fetchGoalDatasBy(GoalData condition) {
        String conditionString = "";
        if (condition.getID() != -1)
            conditionString += ID + "=" + condition.getID() + " AND ";
        if (!Objects.equals(condition.getStartTime(), ""))
            conditionString += START_TIME + "='" + condition.getStartTime() + "' AND ";
        if (!Objects.equals(condition.getEndTime(), ""))
            conditionString += END_TIME + "='" + condition.getEndTime() + "' AND ";
        if (!Objects.equals(condition.getAlertTime(), ""))
            conditionString += ALERT_TIME + "='" + condition.getAlertTime() + "' AND ";
        if (!Objects.equals(condition.getNote(), ""))
            conditionString += NOTE + "='" + condition.getNote() + "' AND ";
        if (condition.getImportance() != -1)
            conditionString += IMPORTANCE + "=" + condition.getImportance() + " AND ";
        if (condition.getType() != -1)
            conditionString += TYPE + "=" + condition.getType() + " AND ";
        if (condition.getFather() != -1)
            conditionString += FATHER + "=" + condition.getFather() + " AND ";
        if (condition.getSonNumber() != -1)
            conditionString += SON_NUMBER + "=" + condition.getSonNumber() + " AND ";
        if (!Objects.equals(condition.getUserName(), ""))
            conditionString += USER_NAME + "='" + condition.getUserName() + "' AND ";
        if (condition.getCompleted() != -1)
            conditionString += COMPLETED + "=" + condition.getCompleted() + " AND ";
        conditionString += ID + " IS NOT NULL";

        Cursor cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor2GoalData(cursor);
    }

    // 根据条件返回所有目标
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<GoalData> fetchAllGoalDatasBy(GoalData condition, int order) {
        String conditionString = "";
        if (condition.getID() != -1)
            conditionString += ID + "=" + condition.getID() + " AND ";
        if (!Objects.equals(condition.getStartTime(), ""))
            conditionString += START_TIME + "='" + condition.getStartTime() + "' AND ";
        if (!Objects.equals(condition.getEndTime(), ""))
            conditionString += END_TIME + "='" + condition.getEndTime() + "' AND ";
        if (!Objects.equals(condition.getAlertTime(), ""))
            conditionString += ALERT_TIME + "='" + condition.getAlertTime() + "' AND ";
        if (!Objects.equals(condition.getNote(), ""))
            conditionString += NOTE + "='" + condition.getNote() + "' AND ";
        if (condition.getImportance() != -1)
            conditionString += IMPORTANCE + "=" + condition.getImportance() + " AND ";
        if (condition.getType() != -1)
            conditionString += TYPE + "=" + condition.getType() + " AND ";
        if (condition.getFather() != -1)
            conditionString += FATHER + "=" + condition.getFather() + " AND ";
        if (condition.getSonNumber() != -1)
            conditionString += SON_NUMBER + "=" + condition.getSonNumber() + " AND ";
        if (!Objects.equals(condition.getUserName(), ""))
            conditionString += USER_NAME + "='" + condition.getUserName() + "' AND ";
        if (condition.getCompleted() != -1)
            conditionString += COMPLETED + "=" + condition.getCompleted() + " AND ";
        conditionString += ID + " IS NOT NULL";
        Cursor cursor;
        cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                null, null, null, END_TIME, null);
        if (order == 1)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, START_TIME, null);
        else if (order == 2)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, IMPORTANCE, null);
        else if (order == 3)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, NAME, null);
        ArrayList<GoalData> ret = new ArrayList<>();
        while (cursor.moveToNext())
            ret.add(cursor2GoalData(cursor));
        return ret;
    }

    // 根据条件(时间范围)返回所有目标
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<GoalData> fetchAllGoalDatasWithTimeBy(GoalData condition, int order, String
            startTime, String endTime) {
        String conditionString = "";
        if (condition.getID() != -1)
            conditionString += ID + "=" + condition.getID() + " AND ";
        if (!Objects.equals(condition.getStartTime(), ""))
            conditionString += START_TIME + "='" + condition.getStartTime() + "' AND ";
        if (!Objects.equals(condition.getEndTime(), ""))
            conditionString += END_TIME + "='" + condition.getEndTime() + "' AND ";
        if (!Objects.equals(condition.getAlertTime(), ""))
            conditionString += ALERT_TIME + "='" + condition.getAlertTime() + "' AND ";
        if (!Objects.equals(condition.getNote(), ""))
            conditionString += NOTE + "='" + condition.getNote() + "' AND ";
        if (condition.getImportance() != -1)
            conditionString += IMPORTANCE + "=" + condition.getImportance() + " AND ";
        if (condition.getType() != -1)
            conditionString += TYPE + "=" + condition.getType() + " AND ";
        if (condition.getFather() != -1)
            conditionString += FATHER + "=" + condition.getFather() + " AND ";
        if (condition.getSonNumber() != -1)
            conditionString += SON_NUMBER + "=" + condition.getSonNumber() + " AND ";
        if (!Objects.equals(condition.getUserName(), ""))
            conditionString += USER_NAME + "='" + condition.getUserName() + "' AND ";
        if (condition.getCompleted() != -1)
            conditionString += COMPLETED + "=" + condition.getCompleted() + " AND ";
        conditionString += ID + " IS NOT NULL";
        conditionString += " AND " + END_TIME + " between " + "'" + startTime + "' AND '" + endTime + "'";
        Cursor cursor;
        cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                null, null, null, END_TIME, null);
        if (order == 1)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, START_TIME, null);
        else if (order == 2)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, IMPORTANCE, null);
        else if (order == 3)
            cursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, conditionString,
                    null, null, null, NAME, null);
        ArrayList<GoalData> ret = new ArrayList<>();
        while (cursor.moveToNext())
            ret.add(cursor2GoalData(cursor));
        return ret;
    }


    //删除所有目标
    public boolean deleteAllGoalDatas() {
        return GoalDatabaseOperator.delete(TABLE_NAME, null, null) > 0;
    }

    // 根据ID删除目标
    public boolean deleteGoalDatabyId(int id) {
        return GoalDatabaseOperator.delete(TABLE_NAME, ID + "= ?", new String[]{String.valueOf
                (id)}) >0;
    }


    // 下面的代码有待重构
    //根据目标名注销
    public boolean deleteGoalDatabyname(String name) {
        return GoalDatabaseOperator.delete(TABLE_NAME, NAME + "= ?", new String[]{name}) > 0;
    }

    // 返回所有目标的某一列
    public String[] getAllGoalsByColumn(String column) {
        if (GoalDatabaseOperator == null) return null;
        @SuppressLint("Recycle") Cursor cursor = GoalDatabaseOperator.query(TABLE_NAME, new
                String[]{column}, null, null, null, null, null);
        ArrayList<String> ret = new ArrayList<String>();
        while (cursor.moveToNext())
            ret.add(cursor.getString(0));
        return ret.toArray(new String[0]);
    }

    // 根据id查询列的值
    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = GoalDatabaseOperator.query(false, TABLE_NAME, null, ID + "=" + id,
                null, null, null, null, null);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }

    // 根据id更新列的值
    public boolean updateGoalDataById(String columnName, int id, String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return GoalDatabaseOperator.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    // 根据目标名找目标，可以判断注册时目标名是否已经存在
    public int findGoalByName(String goalName) {
        int result = 0;
        String[] temp = new String[10];
        temp[0] = goalName;
//        Cursor mCursor = GoalDatabaseOperator.rawQuery("select * from goals where GOAL_NAME like ?", temp);
//        if (mCursor != null) {
//            result = mCursor.getCount();
//            mCursor.close();
//        }
        return result;
    }


}

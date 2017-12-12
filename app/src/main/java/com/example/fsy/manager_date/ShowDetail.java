package com.example.fsy.manager_date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fsy.manager_date.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ShowDetail extends AppCompatActivity {
    private GoalDataManager mUserDataManager;
    private MyAdapter adpt;
    private ArrayList<GoalData> sons;
    private int id;
    private GoalData father;
    private int completed = 0;
    private TextView startTimeTextView, endTimeTextView, alertTimeTextView;
    private CustomDatePicker startDatePicker, endDatePicker, alertDatePicker;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private TextView fatherImportance;

    // 更新子任务列表
    private void updateList() {
        sons = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                "", "", -1, -1, id, -1, "", completed), 0);
        adpt.setSons(sons);
        adpt.notifyDataSetChanged();
        if (father.getType() != 3) {
            ProgressBar fatherProgress = (ProgressBar) findViewById(R.id.progress_bar);
            int unCompleteNumber = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "",
                    "", "", "", -1, -1, id, -1, "", 0), 0).size();
            int completeNumber = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                    "", "", -1, -1, id, -1, "", 1), 0).size();
            if ((completeNumber + unCompleteNumber) == 0)
                fatherProgress.setProgress(0);
            else
                fatherProgress.setProgress(100 * completeNumber / (completeNumber + unCompleteNumber));
        }
    }


    // 子任务列表的adapter
    class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        ArrayList<GoalData> sons;

        public MyAdapter(Context context, ArrayList<GoalData> sons) {
            this.mInflater = LayoutInflater.from(context);
            this.sons = sons;
        }

        public void setSons(ArrayList<GoalData> sons) {
            this.sons = sons;
        }

        @Override
        public int getCount() {
            return sons.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class ViewHolder {
            public TextView text;
            public TextView text2;
            public CheckBox checkBox;
        }

        @SuppressLint({"InflateParams", "CutPasteId"})
        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.parent_item, null);
                holder.text = (TextView) view.findViewById(R.id.parent_title);
                holder.text2 = (TextView) view.findViewById(R.id.parent_due_date);
                holder.checkBox = (CheckBox) view.findViewById(R.id.selected);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.text.setText(sons.get(pos).getName());
            holder.text2.setText(sons.get(pos).getEndTime());
            holder.checkBox.setChecked(false);
            if (completed == 0) holder.checkBox.setChecked(false);
            else holder.checkBox.setChecked(false);
            holder.text.setTag(sons.get(pos).getID());
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) (view.getTag());
                    Intent intent = new Intent(ShowDetail.this, ShowDetail.class);
                    intent.putExtra("id", String.valueOf(id));
                    startActivity(intent);
                }
            });
            holder.checkBox.setTag(sons.get(pos).getID());
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) (view.getTag());
                    GoalData goal = mUserDataManager.fetchGoalDatasByID(id);
                    goal.setCompleted(1 - goal.getCompleted());
                    mUserDataManager.updateGoalData(goal);
                    updateList();
                }
            });
            return view;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        findViewById(R.id.sub_goal_list).setFocusable(false);

        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this, "test");
            mUserDataManager.openGoalDatabase();                              //建立本地数据库
        }

        // 根据ID找到当前任务father
        id = Integer.parseInt(getIntent().getExtras().getString("id"));
        father = mUserDataManager.fetchGoalDatasByID(id);

        // 任务名文本框相关操作
        TextView nameTextView = ((TextView) findViewById(R.id.goal_name));
        nameTextView.setText(father.getName());
        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(ShowDetail.this);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShowDetail.this);
                dialogBuilder.setTitle("修改任务名").setIcon(android.R.drawable.ic_dialog_info).
                        setView(inputServer).setNegativeButton("取消", null);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        ((TextView) findViewById(R.id.goal_name)).setText(name);
                        father.setName(name);
                        mUserDataManager.updateGoalData(father);
                    }
                });
                dialogBuilder.show();
            }
        });


        // 设置进度条相关操作
        final ProgressBar fatherProgress = (ProgressBar) findViewById(R.id.progress_bar);
        int unCompleteNumber = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "",
                "", "", "", -1, -1, id, -1, "", 0), 0).size();
        int completeNumber = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                "", "", -1, -1, id, -1, "", 1), 0).size();
        if ((completeNumber + unCompleteNumber) == 0)
            fatherProgress.setProgress(0);
        else
            fatherProgress.setProgress(100 * completeNumber / (completeNumber + unCompleteNumber));
        if (father.getType() == 3) fatherProgress.setVisibility(View.INVISIBLE);


        // 任务时间文本框相关操作
        startTimeTextView = ((TextView) findViewById(R.id.goal_start_time));
        startTimeTextView.setText(father.getStartTime());
        endTimeTextView = ((TextView) findViewById(R.id.goal_end_time));
        endTimeTextView.setText(father.getEndTime());
        alertTimeTextView = ((TextView) findViewById(R.id.goal_alert_time));
        alertTimeTextView.setText(father.getAlertTime());

        String now = sdf.format(new Date());
        startDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler
                () {
            @Override
            public void handle(String time) {
                startTimeTextView.setText(time);
                father.setStartTime(time);
                mUserDataManager.updateGoalData(father);
            }
        }, "2010-01-01 00:00", now);
        startDatePicker.showSpecificTime(true);
        startDatePicker.setIsLoop(true);
        endDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler
                () {
            @Override
            public void handle(String time) {
                endTimeTextView.setText(time);
                father.setEndTime(time);
                mUserDataManager.updateGoalData(father);
            }
        }, "2010-01-01 00:00", now);
        endDatePicker.showSpecificTime(true);
        endDatePicker.setIsLoop(true);
        alertDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler
                () {
            @SuppressLint("MissingPermission")
            @Override
            public void handle(String time) {
                alertTimeTextView.setText(time);
                father.setAlertTime(time);
                mUserDataManager.updateGoalData(father);
                String calanderURL = "content://com.android.calendar/calendars";
                String calanderEventURL = "content://com.android.calendar/events";
                String calanderRemiderURL = "content://com.android.calendar/reminders";
                if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                    calanderURL = "content://com.android.calendar/calendars";
                    calanderEventURL = "content://com.android.calendar/events";
                    calanderRemiderURL = "content://com.android.calendar/reminders";
                } else {
                    calanderURL = "content://calendar/calendars";
                    calanderEventURL = "content://calendar/events";
                    calanderRemiderURL = "content://calendar/reminders";
                }

                //获取要出入的gmail账户的id
                String calId = "";
                Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                        null, null, null);
                if (userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    calId = userCursor.getString(userCursor.getColumnIndex("_id"));

                }
                ContentValues event = new ContentValues();
                event.put("title", "交流");
                event.put("description", "Frankie受空姐邀请,今天晚上10点以后将在Sheraton动作交流.lol~");
                //插入hoohbood@gmail.com这个账户
                event.put("calendar_id", calId);

                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.HOUR_OF_DAY, 10);
                long start = mCalendar.getTime().getTime();
                mCalendar.set(Calendar.HOUR_OF_DAY, 11);
                long end = mCalendar.getTime().getTime();

                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("hasAlarm", 1);
                event.put("eventTimezone", TimeZone.getDefault().getID().toString());

                Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
                long id = Long.parseLong(newEvent.getLastPathSegment());
                ContentValues values = new ContentValues();
                values.put("event_id", id);
                values.put(CalendarContract.Reminders.MINUTES, 1 * 60 * 24);
                values.put(CalendarContract.Reminders.EVENT_ID, id);
                values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                ContentResolver cr1 = getContentResolver(); // 为刚才新添加的event添加reminder
                cr1.insert(CalendarContract.Reminders.CONTENT_URI, values); // 调用这个方法返回值是一个Uri

            }
        }, "2010-01-01 00:00", now);
        alertDatePicker.showSpecificTime(true);
        alertDatePicker.setIsLoop(true);
        findViewById(R.id.start_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String now = sdf.format(new Date());
                if (Objects.equals(startTimeTextView.getText().toString(), ""))
                    startDatePicker.show(now);
                else startDatePicker.show(startTimeTextView.getText().toString());
            }
        });
        findViewById(R.id.end_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String now = sdf.format(new Date());
                if (Objects.equals(endTimeTextView.getText().toString(), ""))
                    endDatePicker.show(now);
                else endDatePicker.show(endTimeTextView.getText().toString());
            }
        });
        findViewById(R.id.alert_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String now = sdf.format(new Date());
                if (Objects.equals(alertTimeTextView.getText().toString(), ""))
                    alertDatePicker.show(now);
                else alertDatePicker.show(alertTimeTextView.getText().toString());
            }
        });


        // 任务详情文本框相关操作
        ((TextView) findViewById(R.id.goal_description)).setText(father.getNote());
        RelativeLayout note = (RelativeLayout) findViewById(R.id.note);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(ShowDetail.this);
                inputServer.setHeight(500);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShowDetail.this);
                dialogBuilder.setTitle("编辑任务详情").setIcon(android.R.drawable.ic_dialog_info).
                        setView(inputServer).setNegativeButton("取消", null);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String note = inputServer.getText().toString();
                        father.setNote(note);
                        mUserDataManager.updateGoalData(father);
                        ((TextView) findViewById(R.id.goal_description)).setText(note);
                    }
                });
                dialogBuilder.show();
            }
        });


        // 任务优先级文本框相关操作
        final String[] importanceStrings = {"重要且紧急", "重要不紧急",
                "紧急不重要", "不重要不紧急", "无"};
        final int[] importanceColors = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.BLACK};
        fatherImportance = ((TextView) findViewById(R.id.goal_importance));
        fatherImportance.setText(importanceStrings[father.getImportance()]);
        fatherImportance.setTextColor(importanceColors[father.getImportance()]);
        fatherImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShowDetail.this);
                dialogBuilder.setTitle("请选择").setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(importanceStrings, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        father.setImportance(which);
                                        mUserDataManager.updateGoalData(father);
                                        fatherImportance.setText(importanceStrings[father
                                                .getImportance()]);
                                        fatherImportance.setTextColor(importanceColors[father
                                                .getImportance()]);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });


        sons = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                "", "", -1, -1, id, -1, "", completed), 0);
        adpt = new MyAdapter(this, sons);
        ListView smallGoalList = (ListView) findViewById(R.id.sub_goal_list);
        smallGoalList.setAdapter(adpt);
        Button history = (Button) findViewById(R.id.history);
        Button subtasks = (Button) findViewById(R.id.sub_tasks);
        Button addSubTask = (Button) findViewById(R.id.add_sub_task);
        if (father.getType() == 3) {
            history.setVisibility(View.INVISIBLE);
            subtasks.setVisibility(View.INVISIBLE);
            addSubTask.setVisibility(View.INVISIBLE);
        }
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed = 1;
                updateList();
            }
        });
        subtasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed = 0;
                updateList();
            }
        });
        addSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(ShowDetail.this);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShowDetail.this);
                dialogBuilder.setTitle("添加任务").setIcon(android.R.drawable.ic_dialog_info).
                        setView(inputServer).setNegativeButton("取消", null);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        mUserDataManager.insertGoalData(new GoalData(-1, name,
                                "", "", "", "",
                                4, father.getType() + 1, father.getID(), 0, "User", 0));
                        father.setSonNumber(father.getSonNumber() + 1);
                        updateList();
                    }
                });
                dialogBuilder.show();
            }
        });

    }
}

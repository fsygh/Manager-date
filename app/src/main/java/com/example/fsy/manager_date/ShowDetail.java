package com.example.fsy.manager_date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowDetail extends AppCompatActivity {
    private GoalDataManager mUserDataManager;
    private MyAdapter adpt;
    private ArrayList<GoalData> sons;
    private int id;
    private int completed = 0;

    private void updateList() {
        sons = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                "", "", -1, -1, id, -1, "", completed));
        adpt.setSons(sons);
        adpt.notifyDataSetChanged();
    }


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
            holder.text2.setText(sons.get(pos).getName());
            holder.checkBox.setChecked(false);
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
                    goal.setCompleted(1);
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

        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this, "test");
            mUserDataManager.openGoalDatabase();                              //建立本地数据库
        }

        id = Integer.parseInt(getIntent().getExtras().getString("id"));
        GoalData father = mUserDataManager.fetchGoalDatasByID(id);
        ((TextView) findViewById(R.id.goal_name)).setText(father.getName());
        String[] importance = {"Important and Urgent", "Important but not Urgent", "Urgent but " +
                "not Important", "not Important and not Urgent",""};
        ((TextView) findViewById(R.id.goal_importance)).setText(importance[father.getImportance()]);
        ((TextView) findViewById(R.id.goal_due_time)).setText(father.getEndTime());
        ((TextView) findViewById(R.id.goal_start_time)).setText(father.getStartTime());
        ((TextView) findViewById(R.id.goal_alert_time)).setText(father.getAlertTime());
        ((TextView) findViewById(R.id.goal_description)).setText(father.getNote());

        sons = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                "", "", -1, -1, id, -1, "", completed));
        adpt = new MyAdapter(this, sons);
        ListView smallGoalList = (ListView) findViewById(R.id.sub_goal_list);
        smallGoalList.setAdapter(adpt);
        Button history = (Button)findViewById(R.id.history);
        Button subtasks = (Button)findViewById(R.id.sub_tasks);
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

    }
}

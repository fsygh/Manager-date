package com.example.fsy.manager_date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        int id = Integer.parseInt(getIntent().getExtras().getString("id"));

        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this,"test");
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        String[] names = mUserDataManager.getStringByColumnName("small_goal",id).split(" ");
        List<Map<String,Object>> listItem = new ArrayList<>();
        if(names.length!=1)
        {
            for (String name : names) {
                Map<String, Object> showitem = new HashMap<>();
                showitem.put("name", name);
                listItem.add(showitem);
            }
            SimpleAdapter adpt = new SimpleAdapter(
                    this, listItem, R.layout.small_goal_list_item,
                    new String[]{"name"},new int[]{R.id.small_goal_name,} );
            ListView smallGoalList = (ListView) findViewById(R.id.small_goal_list);
            smallGoalList.setAdapter(adpt);

            smallGoalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String result = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(ShowDetail.this, result, Toast.LENGTH_SHORT).show();
                }
            });
        }

        ((TextView)findViewById(R.id.goal_name)).
                setText("Goal Name:"+
                        mUserDataManager.getStringByColumnName("goal_name",id));
        ((TextView)findViewById(R.id.end_time)).
                setText("End Time:"+
                        mUserDataManager.getStringByColumnName("end_time",id));
        ((TextView)findViewById(R.id.goal_description)).
                setText("Goal Description:"+
                        mUserDataManager.getStringByColumnName("goal_description",id));

    }
}

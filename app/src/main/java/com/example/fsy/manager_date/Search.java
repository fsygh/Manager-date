package com.example.fsy.manager_date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by fsy on 2017/12/4.
 */

public class Search extends AppCompatActivity implements View.OnClickListener{

    private SearchView mSearchView;
    private List<Map<String, Object>> listItem;
    private GoalDataManager mUserDataManager;
    private ListView goalList;
    private TextView mback;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this, "test");
            mUserDataManager.openGoalDatabase();                              //建立本地数据库
        }
        mback = (TextView) findViewById(R.id.come_back);
        mback.setOnClickListener(this);
        listItem = new ArrayList<>();
        mSearchView = (SearchView) findViewById(R.id.msearch);
        String[] names = mUserDataManager.getAllGoalsByColumn("name");
        String[] ids = mUserDataManager.getAllGoalsByColumn("_id");
        String[] dates = mUserDataManager.getAllGoalsByColumn("end_time");
        if (names != null) {
            for (int i = 0; i < names.length; i++) {
                Map<String, Object> showitem = new HashMap<>();
                showitem.put("id", ids[i]);
                showitem.put("name", names[i]);
                showitem.put("date", "Due Date:" + dates[i]);
                listItem.add(showitem);
            }
        }
        final SimpleAdapter adpt = new SimpleAdapter(
                this, listItem, R.layout.goal_list_item,
                new String[]{"id", "name", "date"}, new int[]{R.id.goal_id, R.id.goal_name, R.id.goal_date});
        goalList = (ListView) findViewById(R.id.all_goal_list);
        goalList.setAdapter(adpt);
        goalList.setTextFilterEnabled(true);
        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Search.this,
                        ShowDetail.class);
                intent.putExtra("id", (String) (listItem.get(i).get("id")));
                startActivity(intent);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    return false;
                }
                return false;
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.come_back:
                Intent intent = new Intent(Search.this, Welcomepage.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}

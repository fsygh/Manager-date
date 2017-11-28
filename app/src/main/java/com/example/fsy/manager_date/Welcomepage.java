package com.example.fsy.manager_date;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Welcomepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoalDataManager mUserDataManager;
    private List<Map<String,Object>> listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this,"test");
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        String[] names = mUserDataManager.getAllGoalsByColumn("goal_name");
        String[] ids = mUserDataManager.getAllGoalsByColumn("_id");
        String[] dates = mUserDataManager.getAllGoalsByColumn("end_time");
        listItem = new ArrayList<>();
        if(names!=null)
        {
            for(int i=0;i<names.length;i++)
            {
                Map<String,Object> showitem = new HashMap<>();
                showitem.put("id",ids[i]);
                showitem.put("name",names[i]);
                showitem.put("date","Due Date:" + dates[i]);
                listItem.add(showitem);
            }

        }
        SimpleAdapter adpt = new SimpleAdapter(
                this, listItem, R.layout.goal_list_item,
                new String[]{"id","name","date"},new int[]{R.id.goal_id, R.id.goal_name, R.id.goal_date} );
        ListView goalList = (ListView) findViewById(R.id.goal_list);
        goalList.setAdapter(adpt);

        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Welcomepage.this,
                        ShowDetail.class);
                intent.putExtra("id",(String)(listItem.get(i).get("id")));
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
                Intent intent = new Intent(Welcomepage.this,
                        Addpoint.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Welcomepage.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "注销成功", Toast.LENGTH_LONG).show();

        }
        else if(id==R.id.nav_exit)
        {
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

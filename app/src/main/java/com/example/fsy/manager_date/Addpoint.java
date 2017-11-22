package com.example.fsy.manager_date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fsy.manager_date.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fsy on 2017/10/29.
 */

public class Addpoint extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout selectDate, selectTime;
    private TextView currentDate, currentTime;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    private Button backAcitivity,nextPage;
    private EditText text1;
    private CustomToolBar  customToolBar;
    public static String saveText="";
    public  static  String saveTime="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpoint);
        text1=(EditText)findViewById(R.id.textView3_text);
        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);
        selectDate = (RelativeLayout) findViewById(R.id.selectDate);
        selectDate.setOnClickListener(this);
        currentDate = (TextView) findViewById(R.id.currentDate);
        currentTime = (TextView) findViewById(R.id.currentTime);
        nextPage=(Button)findViewById(R.id.next_button);
        nextPage.setOnClickListener(this);
        customToolBar = (CustomToolBar) findViewById(R.id.customToolbar);
        customToolBar.setToolBarClick(new CustomToolBar.ToolBarClick() {
            @Override
            public void leftClick() {
                Intent intent1=new Intent(Addpoint.this,Welcomepage.class);
                saveTime="";
                startActivity(intent1);

            }
            public void rightClick() {}
        });
        initDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDate:
                // 日期格式为yyyy-MM-dd
                customDatePicker1.show(currentDate.getText().toString());
                break;

            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker2.show(currentTime.getText().toString());
                break;

            case R.id.next_button:
                if(text1.getText().toString().equals("")||saveTime.equals(""))
                {
                    AlertDialog.Builder builder  = new  AlertDialog.Builder(Addpoint.this);
                    builder.setTitle("警告" ) ;
                    builder.setMessage("未输入目标名或未设置时间" ) ;
                    builder.setPositiveButton("确定" ,  null );
                    builder.show();
                }
                else {
                    saveText=text1.getText().toString().trim();
                    Intent intent2 = new Intent(Addpoint.this, AddSmallPoint.class);
                    startActivity(intent2);
                }
                break;

        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        currentDate.setText(now.split(" ")[0]);
        currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
                saveTime=time;
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }
}

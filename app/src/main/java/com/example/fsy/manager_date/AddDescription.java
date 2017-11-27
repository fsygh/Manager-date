package com.example.fsy.manager_date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddDescription extends AppCompatActivity implements View.OnClickListener{
    private Button okButton;
    private Button  backButton;
    private CustomToolBar  customToolBar;
    private EditText textDescription;
    public static  String description="";
    private static Addpoint ap=new Addpoint();
    private static AddSmallPoint as=new AddSmallPoint();
    private GoalDataManager mUserDataManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddescription);
        okButton=(Button)findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        textDescription=(EditText)findViewById(R.id.editText1);
        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this,"test");
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        customToolBar = (CustomToolBar) findViewById(R.id.customToolbar);
        customToolBar.setToolBarClick(new CustomToolBar.ToolBarClick() {
            @Override
            public void leftClick() {
                textDescription.setText(textDescription.getText().toString());
                Intent intent1=new Intent(AddDescription.this,AddSmallPoint.class);
                startActivity(intent1);

            }
            public void rightClick() {

            }

        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                description=textDescription.getText().toString();
                dealData();
                Intent intent2=new Intent(AddDescription.this,Welcomepage.class);
                startActivity(intent2);
                break;

        }
    }

    public void dealData()
    {

        GoalData mUser = new GoalData(ap.saveText, as.numberSmallPoint,as.allSmallPoint,description,ap.saveTime);
        mUserDataManager.openDataBase();
        long flag = mUserDataManager.insertGoalData(mUser); //新建用户信息
        if (flag == -1) {
            Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"创建成功" , Toast.LENGTH_SHORT).show();
        }


    }

}
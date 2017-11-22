package com.example.fsy.manager_date;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Register extends AppCompatActivity implements OnClickListener{
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private TextView mSureButton;                       //确定按钮
    private ImageView mCancelButton;                      //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (TextView) findViewById(R.id.register_btn_sure);
        mCancelButton = (ImageView) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(this);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(this);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

    }
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure: {                      //确认按钮的监听事件
                    register_check();
                    break;}
                case R.id.register_btn_cancel: {                    //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;}
            }
        }
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            //检查用户是否存在

            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(Register.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

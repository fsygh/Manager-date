package com.example.fsy.manager_date;

/**
 * Created by fsy on 2017/11/22.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener {

    private TextView mBtnLogin;
    private EditText mid;
    private EditText mpass;
    private View mInputLayout;
    private TextView mBtnSign;
    private UserDataManager mUserDataManager;
    private TextView mSign;
    private SharedPreferences login_sp;
    private ImageView mback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        login_sp = getSharedPreferences("userInfo", 0);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        mInputLayout = findViewById(R.id.input_layout);
        mid=(EditText)findViewById(R.id.idName);
        mpass=(EditText)findViewById(R.id.idPassWord);
        mBtnSign=(TextView)findViewById(R.id.main_btn_sign);
        mback=(ImageView)findViewById(R.id.finishIt);
        mBtnLogin.setOnClickListener(this);
        mBtnSign.setOnClickListener(this);
        mback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.main_btn_login:
                if(isEmpty())
                {
                    String userName = mid.getText().toString().trim();    //获取当前输入的用户名和密码信息
                    String userPwd =mpass.getText().toString().trim();
                    int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
                    SharedPreferences.Editor editor =login_sp.edit();
                    if(result==1){                                             //返回1说明用户名和密码均正确
                        //保存用户名和密码
                        editor.putString("USER_NAME", userName);
                        editor.putString("PASSWORD", userPwd);

                        Intent intent = new Intent(LoginActivity.this,Welcomepage.class) ;    //切换Login Activity至User Activity
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, getString(R.string.login_success),Toast.LENGTH_SHORT).show();//登录成功提示
                    }else if(result==0){
                        Toast.makeText(this, getString(R.string.login_fail),Toast.LENGTH_SHORT).show();  //登录失败提示
                    }

                }
                break;

            case R.id.main_btn_sign:
                Intent intent1=new Intent(LoginActivity.this,Register.class);
                startActivity(intent1);
                break;

            case R.id.finishIt:
                LoginActivity.this.finish();
                break;

        }
    }

    public boolean isEmpty()
    {
        if(mid.getText().toString().trim().equals("")){
        Toast.makeText(this, getString(R.string.account_empty),
                Toast.LENGTH_SHORT).show();
        return false;
        }
        if(mpass.getText().toString().trim().equals("")){
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}

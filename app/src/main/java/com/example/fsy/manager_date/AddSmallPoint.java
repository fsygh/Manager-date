package com.example.fsy.manager_date;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;

/**
 * Created by fsy on 2017/10/31.
 */

public class AddSmallPoint extends AppCompatActivity implements View.OnClickListener{
    private Button add_one;
    private Button delete_one;
    private Button nextButton;
    private Button backButton;
    private EditText text1;
    private  int I=0;
    public static String allSmallPoint="";
    private static RelativeLayout newSingleRL;
    public static int numberSmallPoint=0;
    private static String str="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsmallpoint);
        add_one=(Button)findViewById(R.id.add_one_button);
        add_one.setOnClickListener(this);
        delete_one=(Button)findViewById(R.id.delete_one_button);
        delete_one.setOnClickListener(this);
        nextButton=(Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        backButton=(Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        text1=(EditText)findViewById(R.id.editText1);
    }

    public void onClick(View v) {
        final LinearLayout lin = (LinearLayout) findViewById(R.id.list_Lin);
        final LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newSingleRL = new RelativeLayout(this);
        switch (v.getId()) {
            case R.id.add_one_button:
                final EditText e = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSmallPoint.this).setTitle("请输入！");
                builder.setView(e);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        str = e.getText().toString().trim();
                        if(str.length()==0)
                        {
                            Toast.makeText(AddSmallPoint.this,"输入不能为空",3).show();
                        }
                        else {
                            allSmallPoint+=str+" ";
                            numberSmallPoint++;
                            newSingleRL = generateSingleLayout(id,"小目标"+(++I)+": ", str);
                            lin.addView(newSingleRL, LP_FW);//全部用父结点的布局参数
                        }
                    }
                });
                builder.show();
                break;
            case R.id.delete_one_button:
                if(I>0) {
                    lin.removeViewAt(I--);
                }
                //全部用父结点的布局参数
                break;
            case R.id.back_button:
                numberSmallPoint=0;
                allSmallPoint="";
                Intent intent1=new Intent(AddSmallPoint.this,Addpoint.class);
                startActivity(intent1);
                break;

            case R.id.next_button:
                Intent intent2=new Intent(AddSmallPoint.this,AddDescription.class);
                startActivity(intent2);
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private RelativeLayout generateSingleLayout(int imageID, String str, String aim)
    {
        RelativeLayout layout_root_relative=new RelativeLayout(this);

        LinearLayout layout_sub_Lin=new LinearLayout(this);

        layout_sub_Lin.setOrientation(LinearLayout.HORIZONTAL);
        layout_sub_Lin.setPadding(5, 5, 5, 5);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setText(str);
        LP_WW.setMarginStart(5);
        tv.setTextColor(0xFF000000);
        tv.setTextSize(20);
        tv.setLayoutParams(LP_WW);
        layout_sub_Lin.addView(tv);

        RelativeLayout.LayoutParams RL_MW1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);//尤其注意这个位置，用的是父容器的布局参数
        RL_MW1.addRule(RelativeLayout.RIGHT_OF);
        layout_root_relative.addView(layout_sub_Lin,RL_MW1);

        EditText et=new EditText(this);
        LinearLayout.LayoutParams LP_W1 = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        et.setTextSize(24);
        et.setText(aim);
        et.setLayoutParams(LP_W1);
        layout_sub_Lin.addView(et);



        return layout_root_relative;

    }
}


package com.example.fsy.manager_date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddSmallPoint extends Activity implements View.OnClickListener {
	//适配器
	static ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
	CheckboxAdapter listItemAdapter;
	private ImageButton addOne;
	private EditText text1;
	private Button getvalue1;
	private ImageView mBack;
	public static String allSmallPoint="";
	public static int numberSmallPoint=0;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addsmallpoint);

		mBack=(ImageView)findViewById(R.id.btn_back);
		mBack.setOnClickListener(this);
		getvalue1 = (Button) findViewById(R.id.get_value);
		getvalue1.setOnClickListener(this);
		addOne = (ImageButton) findViewById(R.id.addone);
		addOne.setOnClickListener(this);
		text1 = (EditText) findViewById(R.id.text1);
		//listview

	}


	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.addone:
				if(text1.getText().toString().equals(""))
				{
					Toast.makeText(this, "输入不能为空", Toast.LENGTH_LONG).show();
				}
				else
				{
					ListView list = (ListView) findViewById(R.id.list);
					//存储数据的数组列表
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("friend_username", text1.getText().toString());
					map.put("selected", true);
					//添加数据
					listData.add(map);
					//适配器
					listItemAdapter = new CheckboxAdapter(this, listData);
					list.setAdapter(listItemAdapter);
					text1.setText("");
				}
				break;
			case R.id.get_value:
				HashMap<Integer, Boolean> state = listItemAdapter.state;
				for (int j = 0; j < listItemAdapter.getCount(); j++) {
					System.out.println("state.get(" + j + ")==" + state.get(j));
					if (state.get(j) != null) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> map1 = (HashMap<String, Object>) listItemAdapter.getItem(j);
						String username = map1.get("friend_username").toString();
						allSmallPoint=allSmallPoint+ username+"";
						numberSmallPoint++;
					}
				}
				Intent intent2=new Intent(AddSmallPoint.this,AddDescription.class);
				startActivity(intent2);
				break;

			case R.id.btn_back:
				Intent intent=new Intent(AddSmallPoint.this,Addpoint.class);
				startActivity(intent);
				numberSmallPoint=0;
				allSmallPoint="";

		}
	}
}



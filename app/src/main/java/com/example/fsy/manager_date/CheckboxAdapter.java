package com.example.fsy.manager_date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckboxAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<HashMap<String, Object>> listData;	
	//记录checkbox的状态
	HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();		

	//构造函数
	public CheckboxAdapter(Context context,	ArrayList<HashMap<String, Object>> listData) {
		this.context = context;
		this.listData = listData;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 重写View
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.item, null);

		TextView username = (TextView) convertView.findViewById(R.id.friend_username);
		username.setText((String) listData.get(position).get("friend_username"));

		CheckBox check = (CheckBox) convertView.findViewById(R.id.selected);		
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					state.put(position, isChecked);					
				} else {
					state.remove(position);				
				}
			}
		});
		check.setChecked(true);
		return convertView;
	}
}
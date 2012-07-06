package com.rushfusion.tvtopicclient.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter {
	
	private  Context myContext;
	private  int mylayoutid;
	private ArrayList  myList;

	public  void MyAdapter(Context context ,int layoutid,ArrayList list) {
		this.myContext=context;
		this.mylayoutid=layoutid;
		this.myList=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}

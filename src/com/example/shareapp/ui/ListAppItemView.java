package com.example.shareapp.ui;

import com.example.shareapp.R;
import com.example.shareapp.R.id;
import com.example.shareapp.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListAppItemView extends RelativeLayout{
	private TextView app_name = null;
	public ListAppItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ListAppItemView(Context context,String appname) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.list_item , this );
		app_name = (TextView) view.findViewById(R.id.app_name);
		setAppname(appname);
	}
	public void setAppname(String app_name){
		this.app_name.setText(app_name);
	}
}

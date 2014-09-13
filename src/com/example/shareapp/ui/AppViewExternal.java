package com.example.shareapp.ui;

import com.example.shareapp.R;
import com.example.shareapp.R.id;
import com.example.shareapp.R.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppViewExternal extends RelativeLayout{
	private ImageView image = null;
	private TextView appName = null;
	public AppViewExternal(Context context,Bitmap appImage,String name) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.grid_item_external , this );
		image = (ImageView) view.findViewById(R.id.appicon);
		appName = (TextView) view.findViewById(R.id.appname);
		image.setImageBitmap(appImage);
		appName.setText(name);
	
	}
	public void setAppName(String appname){
		appName.setText(appname);
	}
	public void setAppImage(Bitmap bitmap){
		image.setImageBitmap(bitmap);
	}
}

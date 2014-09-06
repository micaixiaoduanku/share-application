package com.example.shareapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shareapp.HandlerControl.HandlerListener;


public class ShareActivity extends Activity implements HandlerListener{
	private ArrayList<AppInfoItem> appearAppInfoItemList = new ArrayList<AppInfoItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HandlerControl.getInstance().addHandlerListener(this);
		AppDataHelper.getInstance().getInstalledPackageTitles(this);
		setContentView(R.layout.activity_main);
	}
	
	
	private void filterResolve(String str,ArrayList<AppInfoItem> list){
		appearAppInfoItemList.clear();
		for(AppInfoItem appitem : list){
			if(appitem.getAppname().contains(str)){
				appearAppInfoItemList.add(appitem);
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onTrigger(TriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		int triId = triggerInfo.GetTriggerID();
		switch(triId){
		 case TriggerID.MESSAGE_GET_COMPALTED:
			 String result = triggerInfo.GetString1();
			 String retcode = result.substring(0, result.indexOf(';'));
			 String appName = result.substring(retcode.length()+1,result.indexOf('-'));
			 String url = result.substring(retcode.length()+appName.length()+2, result.length());
			 Log.i("tag", "result "+result);
			 Log.i("tag", "retcode "+retcode);
			 Log.i("tag", "appName "+appName);
			 Log.i("tag", "url "+url);
				String sendMsg = appName+ " - "+url;
				Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
				intent2.setType("text/plain");
				intent2.putExtra(Intent.EXTRA_TEXT, sendMsg );  
				startActivity(Intent.createChooser(intent2, "Share via"));
				Toast.makeText(this, sendMsg, 1000).show();
			
			break;
		}
	}
}

package com.example.shareapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.shareapp.HandlerControl.HandlerListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class ShareActivity extends Activity implements HandlerListener{
	private Button share_Btn = null;
	private EditText appName = null;
	private EditText message = null;
	private ListView list_prompt = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		HandlerControl.getInstance().addHandlerListener(this);
		AppDataHelper.getInstance().getInstalledPackageTitles(this);
		findViews();
		setListeners();
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		for(AppInfoItem appitem : AppDataHelper.getInstance().getAppInfoItemList()){
			HashMap<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("appname", appitem.getAppname());
			data.add(hashmap);
		}
		list_prompt.setAdapter(new SimpleAdapter(this, data, R.layout.list_item, new String[]{"appname"}, new int[]{R.id.app_name}));
	}
	private void setListeners(){
		share_Btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppInfoItem appInfoItem = AppDataHelper.getInstance().getAppInfoItem(appName.getText().toString());
				if(appInfoItem != null){
					String packageName = appInfoItem.getPkgname();
					HttpHelper.getInstance().startShare(packageName);
				}
				
			}
		});
		appName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() == 0){
					list_prompt.setVisibility(View.GONE);
				}else{
					list_prompt.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		list_prompt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				appName.setText(AppDataHelper.getInstance().getAppInfoItemList().get(position).getAppname());
				list_prompt.setVisibility(View.GONE);
			}
		});
	};
	
	private void filterResolve(String str){
		
	}

	private void findViews(){
		share_Btn = (Button)findViewById(R.id.share_btn);
		appName = (EditText)findViewById(R.id.app_name);
		message = (EditText)findViewById(R.id.message);
		list_prompt = (ListView)findViewById(R.id.fuzzy_prompt);
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
			 try {
				JSONObject json = new JSONObject(triggerInfo.GetString1());
				int returnCode =json.getInt("retcode");
				if(returnCode == 0){
					String url = json.getString("short");
					String msg = message.getText().toString();
					String sendMsg = msg+" 请点击: "+url;
					Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
					intent2.setType("text/plain");
					intent2.putExtra(Intent.EXTRA_TEXT, sendMsg );  
					startActivity(Intent.createChooser(intent2, "Share via"));
					Toast.makeText(this, sendMsg, 1000).show();
					
				}else{				
					Toast.makeText(this, "分享失败", 1000).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
	}
	
}

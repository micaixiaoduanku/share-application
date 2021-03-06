package com.example.shareapp.ui;

import java.util.ArrayList;

import com.example.shareapp.R;
import com.example.shareapp.R.layout;
import com.example.shareapp.R.menu;
import com.example.shareapp.control.AppDataHelper;
import com.example.shareapp.data.AppInfoItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class ShareActivity extends Activity{
	private ArrayList<AppInfoItem> appearAppInfoItemList = new ArrayList<AppInfoItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppDataHelper.getInstance().getInstalledPackageTitles(this);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

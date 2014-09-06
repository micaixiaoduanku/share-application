package com.example.shareapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppDataHelper {
	public static AppDataHelper instance = null;
	private ArrayList<AppInfoItem> appInfoItemList = new ArrayList<AppInfoItem>();
	private AppDataHelper(){}
	public static AppDataHelper getInstance(){
		if(instance == null){
			instance = new AppDataHelper();
		}
		return instance;
	}
	public ArrayList<AppInfoItem> getInstalledPackageTitles(Context context){
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		final List<ResolveInfo> packages = context.getPackageManager().queryIntentActivities( mainIntent, 0);
		for(ResolveInfo packageInfo : packages){
			//filter system application 
			if((packageInfo.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){		
				String appName = packageInfo.activityInfo.loadLabel(context.getPackageManager()).toString();
				String pkgName =  packageInfo.activityInfo.packageName;
				Drawable d = packageInfo.activityInfo.loadIcon(context.getPackageManager());
				Bitmap bitmap = Utilities.createIconBitmap(d, context);
				boolean isaddTolist = true;
				if(pkgName.equals(context.getPackageName())){
					isaddTolist = false;
				}
				if(isaddTolist){
					AppInfoItem appitem = new AppInfoItem(appName, pkgName,bitmap);
					appInfoItemList.add(appitem);
				}
			}
		}
		return appInfoItemList;
	}
	public AppInfoItem getAppInfoItem(String appname){
		for(AppInfoItem appitem : appInfoItemList){
			if(appname.equals(appitem.getAppname())){
				return appitem;
			}
		}
		return null;
	}
	public ArrayList<AppInfoItem> getAppInfoItemList(){
		return appInfoItemList;
	}
	
}

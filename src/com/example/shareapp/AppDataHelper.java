package com.example.shareapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

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
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for(PackageInfo packageInfo : packages){
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
				String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
				String pkgName = packageInfo.packageName; 
				AppInfoItem appitem = new AppInfoItem(appName, pkgName);
				appInfoItemList.add(appitem);
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

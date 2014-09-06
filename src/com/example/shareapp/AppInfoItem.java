package com.example.shareapp;

import android.graphics.Bitmap;


public class AppInfoItem {
	private String appname = "";
	private String pkgname = "";
	private String url = "";
	private Bitmap icon;
	public AppInfoItem(String appname,String pkgname,Bitmap icon){
		this.setAppname(appname);
		this.setPkgname(pkgname);
		this.setIcon(icon);
	}
	public String getPkgname() {
		return pkgname;
	}
	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bitmap getIcon() {
		return icon;
	}
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
}

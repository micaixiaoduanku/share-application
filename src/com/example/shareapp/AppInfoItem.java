package com.example.shareapp;


public class AppInfoItem {
	private String appname = "";
	private String pkgname = "";
	private String url = "";
	public AppInfoItem(String appname,String pkgname){
		this.setAppname(appname);
		this.setPkgname(pkgname);
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
}

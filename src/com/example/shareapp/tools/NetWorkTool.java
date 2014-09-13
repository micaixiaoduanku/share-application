package com.example.shareapp.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkTool {
	public static boolean isWifiEnabled(
			Context context )
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if( networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI )
		{
			return true;
		}
		return false;
	}
	public static boolean is3GEnabled(
			Context context )
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if( networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE )
		{
			return true;
		}
		return false;
	}
}

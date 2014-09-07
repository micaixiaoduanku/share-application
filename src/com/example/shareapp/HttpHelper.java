package com.example.shareapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;


public class HttpHelper {
	private String url_str = "http://tjx.be/sapk/s.php?p=";
	private static HttpHelper instance;
	private HttpHelper(){
		
	}
	public static HttpHelper getInstance(){
		if(instance == null){
			instance = new HttpHelper();
		}
		return instance;
	}
	
	public void startShare(final AppInfoItem appinfoitem){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				String result = "";
				try{
					URL url = new URL(url_str+appinfoitem.getPkgname());
					HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
					InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
					BufferedReader bufferedReader = new BufferedReader(in);
					String readLine = null;
					while((readLine = bufferedReader.readLine()) != null){
						result += readLine;
					}
					in.close();
					urlConn.disconnect();
				}catch(Exception e){
					e.printStackTrace();
					result = "conn fail";
				}finally{
					
				}
				TriggerInfo tri = new TriggerInfo(TriggerID.MESSAGE_GET_COMPALTED);
				try {
					JSONObject json = new JSONObject(result);
					int returnCode =json.getInt("retcode");
					String url = json.getString("short");
					tri.m_String1 = returnCode+";"+appinfoitem.getAppname()+"-"+url;					
					HandlerControl.getInstance().sendTrigger(tri);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}.start();
	}
	
	protected HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion( params , HttpVersion.HTTP_1_1 );
		HttpProtocolParams.setContentCharset( params ,  HTTP.UTF_8 );
		HttpProtocolParams.setUseExpectContinue( params , true );
		HttpProtocolParams.setUserAgent( params , "Android 2.2.1" );
		// 超时设置
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout( params , 5000 );
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout( params , 10000 );
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout( params , 10000 );
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register( new Scheme( "http" , (SocketFactory)PlainSocketFactory.getSocketFactory() , 80 ) );
		schReg.register( new Scheme( "https" , (SocketFactory)SSLSocketFactory.getSocketFactory() , 443 ) );
		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager( params , schReg );
		return new DefaultHttpClient( conMgr , params );
	}
}

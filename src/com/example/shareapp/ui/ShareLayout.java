package com.example.shareapp.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shareapp.R;
import com.example.shareapp.control.AppDataHelper;
import com.example.shareapp.control.HandlerControl;
import com.example.shareapp.control.HandlerControl.HandlerListener;
import com.example.shareapp.control.HttpHelper;
import com.example.shareapp.data.AppInfoItem;
import com.example.shareapp.data.TriggerID;
import com.example.shareapp.data.TriggerInfo;
import com.example.shareapp.tools.NetWorkTool;

public class ShareLayout extends RelativeLayout implements HandlerListener{
	private GridView gridview = null;
	private EditText srchEdit = null;
	private RelativeLayout srchBtn = null;
	private GridViewAdpter gridViewAdpter = null;
	private ArrayList<AppInfoItem> appearApplicationInfoList = new ArrayList<AppInfoItem>();
	private boolean isSharing = false;
	public ShareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.external_add_layout , this );
		gridview = (GridView)view.findViewById(R.id.app_gridview);
		srchEdit = (EditText)view.findViewById(R.id.srch_edit);
		srchBtn = (RelativeLayout)view.findViewById(R.id.srch_btn);
		gridViewAdpter = new GridViewAdpter(getContext());
		gridview.setAdapter(gridViewAdpter);
		gridview.setNumColumns(4);
		setListeners();
		HandlerControl.getInstance().addHandlerListener(this);
		filter("");
	}
	
	private void setListeners(){
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(NetWorkTool.is3GEnabled(getContext()) || NetWorkTool.isWifiEnabled(getContext())){
					if(!isSharing){
						HttpHelper.getInstance().startShare(appearApplicationInfoList.get(position));
					}	
				}
				else{
					Toast.makeText(getContext(), R.string.network_error_tips, Toast.LENGTH_SHORT).show();
				}
			}
		});
		gridview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(srchEdit.getWindowToken(), 0);
				return false;
			}
		});
		srchEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				filter(s.toString());
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
		srchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(srchEdit.getVisibility() == View.GONE || srchEdit.getVisibility() == View.INVISIBLE){
					srchEdit.setVisibility(View.VISIBLE);		
					srchEdit.setFocusable(true);
					srchEdit.requestFocus();
					InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(srchEdit, InputMethodManager.SHOW_IMPLICIT);
				}
			}
		});
	}
	
	private void filter(String appname){
		appearApplicationInfoList.clear();
		for(AppInfoItem appitem : AppDataHelper.getInstance().getAppInfoItemList()){
			if(appitem.getAppname().contains(appname)){
				appearApplicationInfoList.add(appitem);
			}
		}
		gridViewAdpter.notifyDataSetChanged();
	}
	
	class GridViewAdpter extends BaseAdapter{
		private Context context = null;
		public GridViewAdpter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appearApplicationInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				Bitmap bitmap =appearApplicationInfoList.get(position).getIcon();
				String title = appearApplicationInfoList.get(position).getAppname();				
				convertView = new AppViewExternal(context, bitmap,title);
			}else{
				((AppViewExternal)convertView).setAppImage(appearApplicationInfoList.get(position).getIcon());
				((AppViewExternal)convertView).setAppName(appearApplicationInfoList.get(position).getAppname());
			}
			return convertView;
		}
		
	}

	@Override
	public void onTrigger(TriggerInfo triggerInfo) {
		// TODO Auto-generated method stub
		int triId = triggerInfo.m_iTriggerID;
		switch(triId){
		case TriggerID.MESSAGE_GET_COMPALTED:
			 String result = triggerInfo.GetString1();
			 String retcode = result.substring(0, result.indexOf(';'));
			 switch(Integer.parseInt(retcode)){
			 	case 1:
			 		Toast.makeText(getContext(), getContext().getString(R.string.share_fail_exception_tips)+retcode, Toast.LENGTH_SHORT).show();
					 isSharing = false;
			 		return;
			 	case -1:
			 		Toast.makeText(getContext(), getContext().getString(R.string.share_fail_notfound_res_form_server_tips)+retcode, Toast.LENGTH_SHORT).show();
					 isSharing = false;
					 return;
			 	default:
			 		break;
			 }
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
			getContext().startActivity(Intent.createChooser(intent2, "Share via"));
			Toast.makeText(getContext(), sendMsg, 1000).show();
			isSharing = false;
			break;
		}
	}
}

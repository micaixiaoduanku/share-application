package com.example.shareapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ShareLayout extends RelativeLayout{
	private GridView gridview = null;
	private EditText srchEdit = null;
	private ImageView srchBtn = null;
	private GridViewAdpter gridViewAdpter = null;
	private ArrayList<AppInfoItem> externalApplicationInfoList;
	public ShareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		externalApplicationInfoList = AppDataHelper.getInstance().getAppInfoItemList();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View view = inflater.inflate( R.layout.external_add_layout , this );
		gridview = (GridView)view.findViewById(R.id.app_gridview);
		srchEdit = (EditText)view.findViewById(R.id.srch_edit);
		srchBtn = (ImageView)view.findViewById(R.id.srch_btn);
		gridViewAdpter = new GridViewAdpter(getContext());
		gridview.setAdapter(gridViewAdpter);
		gridview.setNumColumns(4);
		setListeners();
	}
	
	private void setListeners(){
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
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
	
	class GridViewAdpter extends BaseAdapter{
		private Context context = null;
		public GridViewAdpter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return externalApplicationInfoList.size();
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
				Bitmap bitmap =externalApplicationInfoList.get(position).getIcon();
				String title = externalApplicationInfoList.get(position).getAppname();				
				convertView = new AppViewExternal(context, bitmap,title);
			}else{
				((AppViewExternal)convertView).setAppImage(externalApplicationInfoList.get(position).getIcon());
				((AppViewExternal)convertView).setAppName(externalApplicationInfoList.get(position).getAppname());
			}
			return convertView;
		}
		
	}
}

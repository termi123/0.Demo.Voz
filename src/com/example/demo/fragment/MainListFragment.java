package com.example.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.demo.voz.MainActivity;
import com.example.demo.voz.R;
import com.example.demo.voz.R.layout;

public class MainListFragment extends SherlockFragment{
	private View view;
	public final static String INTENT_VAR_IS_RELATED = "intentVarIsRelated";
	
	public void setMainActivity(MainActivity mainActivity) {
	}
	
	public void onStart() {
		super.onStart();
		init();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.fragment_main_list, container, false);
		
		return view;
	}
	
	private void init() {
	}
	
}

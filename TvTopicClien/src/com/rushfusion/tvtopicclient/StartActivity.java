package com.rushfusion.tvtopicclient;

import java.util.HashMap;


import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class StartActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		HashMap<String,Object> mparms = new HashMap<String, Object>();
		mparms.put("PROGRAM_NAME", "ss");
		ViewGroup mRoot = (ViewGroup) getWindow().getDecorView();
		TvTopicClient y = new TvTopicClient();
		y.init(this, mRoot,mparms);
		y.run();
	}

}

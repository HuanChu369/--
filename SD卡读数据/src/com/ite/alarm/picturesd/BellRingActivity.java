package com.ite.alarm.picturesd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

/**
 * 闹钟铃响的窗体
 * 
 */

public class BellRingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bellring_activity);
		
		// 获取Intent对象
		Intent intent = this.getIntent();
		
		// 接收上个界面传递过来的路径
		String imagePath = intent.getStringExtra("imagePath");
		
		// 获取布局中最外层的layout设置背景，其中BitmapDrawable.createFromPath(imagePath)这句可以通过路径获取到图片对象
		this.findViewById(R.id.layoutBellRing).setBackground(
				BitmapDrawable.createFromPath(imagePath));
	}

}

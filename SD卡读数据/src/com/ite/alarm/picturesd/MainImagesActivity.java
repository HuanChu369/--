package com.ite.alarm.picturesd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

public class MainImagesActivity extends Activity {

	// 定义一个list和map，后面会用到
	List<Map<String, Object>> list;
	HashMap<String, Object> map;
	
	// 定义GridView组件
	GridView gvImages;

	/**
	 * 重写onCreate方法
	 * 
	 */
	
	@SuppressWarnings("unused")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 绑定组件
		gvImages = (GridView) findViewById(R.id.gvImages);
		
		list = new ArrayList<Map<String, Object>>();
		ContentResolver contentResolver = getContentResolver();
		
		// 使用query方法，查看数据
		final Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI,
				null, null, null, Media.DATE_TAKEN);
		
		// 将数据存在游标的相关位置，便于读取
		final int imageName = cursor.getColumnIndex(Media.DISPLAY_NAME);
		final int imagePath = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		int imageDate = cursor.getColumnIndex(Media.DATE_TAKEN);
		final int imageSize = cursor.getColumnIndex(Media.SIZE);
		
		// 将游标移动到开始位置
		if (cursor.moveToFirst()) {

			do {
				
				// 添加数据
				map = new HashMap<String, Object>();
				map.put("imagePath", cursor.getString(imagePath));// 这里你原来将压缩后的图片存储，后来我改成了路径了
				map.put("imageName", cursor.getString(imageName));
				list.add(map);

			} while (cursor.moveToNext());

			/**
			 * 定义适配器
			 */
			
			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.grid_activity, new String[] { "imagePath",
							"imageName" }, new int[] { R.id.image,
							R.id.tvimgName });
			
			// 绑定适配器
			adapter.setViewBinder(new ViewBinder() {

				/**
				 * 将压缩的图片添加进适配器
				 */
				
				@Override
				public boolean setViewValue(View v, Object data, String text) {
					if (v instanceof ImageView && data instanceof Bitmap) {
						ImageView i = (ImageView) v;
						i.setImageBitmap((Bitmap) data);
						return true;// 返回true才能添加进适配器
					}
					return false;
				}
			});
			
			// 装载适配器
			gvImages.setAdapter(adapter);

			/**
			 * 为适配器添加列表选项的点击事件
			 */
			
			// 提示点击图片设置背景
			Toast.makeText(getApplicationContext(), "点击图片设置背景",
					Toast.LENGTH_LONG).show();
			gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
				/**
				 * 点击事件俺的主要作用就是更改背景
				 */
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// 点击图片设置背景
					Intent intent = new Intent(MainImagesActivity.this,
							BellRingActivity.class);
					
					// 获取上面存储了对象的list集合
					HashMap<String, Object> map = (HashMap<String, Object>) list
							.get(position);
					
					// 获取集合中的路径，传递给BellRingActivity
					intent.putExtra("imagePath", map.get("imagePath")
							.toString());
					
					// 执行跳转
					startActivity(intent);
				}

			});
		}
	}

	/**
	 * 压缩图片
	 * 
	 */
	public Bitmap compressImage(String path) {
		Bitmap image = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 20;// 每次都减少20
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;

	}
}
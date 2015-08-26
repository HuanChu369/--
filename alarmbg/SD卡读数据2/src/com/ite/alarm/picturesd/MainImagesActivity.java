package com.ite.alarm.picturesd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MainImagesActivity extends Activity {

	// 定义一个list和map
	List<Map<String, Object>> list;
	HashMap<String, Object> map;
	// 定义组件
	GridView gvImages;

	public Activity mActivity;

	/**
	 * 重写onCreate方法
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 绑定组件
		gvImages = (GridView) findViewById(R.id.gvImages);
		list = new ArrayList<Map<String, Object>>();
		ContentResolver contentResolver = getContentResolver();
		// 查看数据
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

			gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// 点击图片设置背景

					/*
					 * LayoutInflater inflater = getLayoutInflater(); View
					 * layout = inflater.inflate(R.layout.dialog, null); Dialog
					 * dialog = new Dialog(MainImagesActivity.this);
					 * dialog.setContentView(layout);// 给弹窗配置自定义布 ImageView
					 * ivImages = (ImageView) layout
					 * .findViewById(R.id.ivImages); // Button btnSet = (Button)
					 * findViewById(R.id.btnSet);
					 * 
					 * cursor.moveToPosition(position);
					 * ivImages.setImageBitmap(BitmapFactory.decodeFile((cursor
					 * .getString(imagePath))));
					 * dialog.setTitle(cursor.getString(imageName));
					 * dialog.show();
					 */
					// Toast.makeText(getApplication(), "",
					// Toast.LENGTH_SHORT).show();
					// byte[] bytes = new byte[(int) imageSize];
					// LayoutInflater layoutInflater = getLayoutInflater();
					// View layoutRing = layoutInflater.inflate(
					// R.layout.bellring_activity, null);
					// mActivity.addContentView(layoutRing, null);
					// mActivity.getWindow().setBackgroundDrawable(getWallpaper());
					// 这里new一个intent
					Intent intent = new Intent(MainImagesActivity.this,
							BellRingActivity.class);
					// 获取上面你存储了对象的list集合
					HashMap<String, Object> map = (HashMap<String, Object>) list
							.get(position);
					// 获取集合中的路径，传递给BellRingActivity
					intent.putExtra("imagePath", map.get("imagePath")
							.toString());
					// 执行跳转
					startActivity(intent);
				}
			});

			/**
			 * 设置长点击事件---设置背景
			 */
			/*
			 * gvImages.setOnItemLongClickListener(new
			 * AdapterView.OnItemLongClickListener() {
			 * 
			 * 
			 * @Override public boolean onItemLongClick(AdapterView<?> parent,
			 * View view, int position, long id) { byte[] bytes = new byte[(int)
			 * imageSize]; LinearLayout layout = (LinearLayout)
			 * findViewById(R.id.layoutBellRing); Bitmap bitmap =
			 * BitmapFactory.decodeByteArray(bytes, 0, imageSize);
			 * BitmapDrawable bd = new BitmapDrawable(,bitmap);
			 * layout.setBackground(bd);
			 * layout.setBackgroundResource(imagePath); LayoutInflater inflater
			 * = getLayoutInflater(); View layoutBell =
			 * inflater.inflate(R.layout.bellring_activity, null); // Dialog
			 * dialog = new Dialog(BellRingActivity.class);
			 * mActivity.setContentView(layoutBell); // mActivity
			 * .getWindow().setBackgroundDrawable(cursor.getString(imagePath));
			 * // layout.setBackgroundDrawable(null);
			 * Toast.makeText(getApplicationContext(), "设置背景成功！",
			 * Toast.LENGTH_SHORT).show(); Intent intent = new
			 * Intent(MainImagesActivity.this,BellRingActivity.class);
			 * Log.v("长点击", "长点击...");
			 * 
			 * 
			 * LayoutInflater inflater = getLayoutInflater(); View layoutRing =
			 * inflater.inflate(R.layout.bellring_activity, null); Dialog
			 * dialogRing = new Dialog(MainImagesActivity.this);
			 * dialogRing.setContentView(layoutRing);// 给弹窗配置自定义布局 ImageView
			 * ivImages = (ImageView) layout .findViewById(R.id.ivImages); //
			 * Button btnSet = (Button) findViewById(R.id.btnSet);
			 * 
			 * cursor.moveToPosition(position);
			 * 
			 * //ivImages.setImageBitmap(BitmapFactory.decodeFile((cursor //
			 * .getString(imagePath)))); dialogRing.setTitle("闹钟");
			 * dialogRing.setContentView(cursor.getString(imagePath));
			 * dialogRing.show(); //Toast.makeText(getApplication(), "长按图片设置背景",
			 * Toast.LENGTH_SHORT).show(); return true; }
			 * 
			 * });
			 */

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
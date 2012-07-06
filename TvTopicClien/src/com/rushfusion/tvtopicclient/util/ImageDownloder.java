package com.rushfusion.tvtopicclient.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageDownloder {
	
	public Bitmap imageDownloder(String imagePath) throws Exception{
		  URL url = new URL(imagePath);
		  HttpURLConnection  conn =(HttpURLConnection) url.openConnection();
		  //设置连接超时
		  conn.setConnectTimeout(60*1000); 
		  //设置从服务器读取数据时超时
		  conn.setReadTimeout(60*1000);
		  InputStream is = conn.getInputStream();
		  Bitmap bitmap = BitmapFactory.decodeStream(is);
		 return bitmap;
	}
	 

}

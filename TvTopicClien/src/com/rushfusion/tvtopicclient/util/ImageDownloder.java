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
		  //�������ӳ�ʱ
		  conn.setConnectTimeout(60*1000); 
		  //���ôӷ�������ȡ����ʱ��ʱ
		  conn.setReadTimeout(60*1000);
		  InputStream is = conn.getInputStream();
		  Bitmap bitmap = BitmapFactory.decodeStream(is);
		 return bitmap;
	}
	 

}

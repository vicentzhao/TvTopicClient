package com.rushfusion.tvtopicclient.util;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
public class SourceDownLoader {
    private static JsonUtil ju ;
	public static JSONArray getall(String path) throws Exception {
        URL url =new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setReadTimeout(5*1000);
        InputStream is = conn.getInputStream();
        @SuppressWarnings("static-access")
		String  json = ju.InputStreamTOString(is);
        System.out.println("下载下来的数据是=========>>"+json);
        JSONArray js = new JSONArray(json);
		return js;  
	}
	public static String getallString(String path) throws Exception {
        URL url =new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        InputStream is = conn.getInputStream();
        String  json = ju.InputStreamTOString(is);
		return json;  
	}
	public static InputStream getStream(String path) throws Exception {
		URL url =new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream is = conn.getInputStream();
		return is;  
	}
}

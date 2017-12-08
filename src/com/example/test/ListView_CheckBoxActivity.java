package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.View.OnClickListener;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListView_CheckBoxActivity extends Activity {
	// 适配器
	CheckboxAdapter listItemAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 按钮及事件响应
		Button getValue = (Button) findViewById(R.id.friendlistbutton_deletefriend);
		getValue.setOnClickListener(listener);
		// listview
		ListView list = (ListView) findViewById(R.id.personal_listView);
		// 存储数据的数组列表
		ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
		String[] name = { "William", "Charles", "Linng", "Json", "Bob",
				"Carli", "William", "Charles", "Linng", "Json", "Bob", "Carli" };
		String[] id = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12" };
		for (int i = 0; i < 12; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("friend_image", R.drawable.beijing);
			map.put("friend_username", name[i]);
			map.put("friend_id", id[i]);
			map.put("selected", false);
			// 添加数据
			listData.add(map);
		}
		// 适配器
		listItemAdapter = new CheckboxAdapter(this, listData);
		list.setAdapter(listItemAdapter);
	}

	// 事件响应
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HashMap<Integer, Boolean> state = listItemAdapter.state;
			String options = "选择的项是:";
			for (int j = 0; j < listItemAdapter.getCount(); j++) {
				System.out.println("state.get(" + j + ")==" + state.get(j));
				if (state.get(j) != null) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> map = (HashMap<String, Object>) listItemAdapter
							.getItem(j);
					String username = map.get("friend_username").toString();
					String id = map.get("friend_id").toString();
					options += "\n" + id + "." + username;
				}
			}
			// 显示选择内容
			Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG)
					.show();
		}
	};
}
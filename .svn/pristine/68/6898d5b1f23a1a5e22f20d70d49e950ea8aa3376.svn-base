package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

@SuppressLint("NewApi")
public class Tanchu extends Activity {
	/** Called when the activity is first created. */
	private String addfriendurl = "http://104.131.19.115/rainbox/frontendAPI/friendListAPI/addFriend.php";
	private EditText add;
	private String addtext;
	private String idnum;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  Intent Myintent = getIntent();
		 idnum= Myintent.getStringExtra("userid"); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		add = (EditText) findViewById(R.id.tanchu_editText);
		add.addTextChangedListener(addtextlistener);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View myLoginView = layoutInflater.inflate(R.layout.activity_tanchu,
				null);

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle("Add Friend")
				.setIcon(R.drawable.ic_launcher)
				.setView(myLoginView)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								try {
									sendrequest(idnum,addtext);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}). setNegativeButton("cancle", new DialogInterface.OnClickListener() {

						     @Override
						     public void onClick(DialogInterface dialog, int which) {
						      // TODO Auto-generated method stub
						     }
						    }).create();
		alertDialog.show();
	}

	private TextWatcher addtextlistener = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			addtext = s.toString();
		}
	};


	private int sendrequest(String username, String friendid)
			throws IOException {
		InputStream result = null;
		URL request = new URL(
				"http://104.131.19.115/rainbox/frontendAPI/friendListAPI/addFriend.php");
		Log.v("send", "start");
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("userId", username);
		// params.put("reply_to_thread", 10394);
		params.put("friendId", friendid);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
					"UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		result = conn.getInputStream();

		Log.e("connect1", result.toString());
		// String encoding = conn.getContentEncoding();
		// encoding = encoding == null ? "UTF-8" : encoding;
		// String body =encoding.toString();
		// String body = IOUtils.toString(result, encoding);
		String temp = readIt(result, 300);
		Log.v("connect1", temp);
		// checksconnectstatus(temp);
		// Reader in = new Bu÷feredReader(new
		// InputStreamReader(conn.getInputStream(), "UTF-8"));
		// for (int c; (c = in.read()) >= 0; System.out.print((char)c));

		// Toast.makeText(this, "Your post was successfully uploaded",
		// Toast.LENGTH_LONG).show();
		Log.v("send", "end");
		return 1;
	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		Log.e("read", reader.toString());

		char[] buffer = new char[len];
		reader.read(buffer);
		// Log.e("read", reader.read(buffer)+"");
		return new String(buffer);
	}

}
package com.example.test;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements Button.OnClickListener{
	//public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private Button forget; 
	private Button register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		forget =(Button)findViewById(R.id.mainbutton_forget);
		forget.setOnClickListener(this);
		register = (Button)findViewById(R.id.mainbutton_register);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.friendlistcheckBox1) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View view){
		if(view==forget){
			Intent intent = new Intent(this, Forgetpassword.class);
			startActivity(intent);
		}
		else if(view == register){
			Intent intent = new Intent(this, Register.class);
			startActivity(intent);
		}
	}
	
}

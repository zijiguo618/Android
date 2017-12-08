package com.example.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("NewApi")
public class Friend_list extends ListActivity {
    CustomAdapter adapter;
	private String friendlisturl = 		"http://104.131.19.115/rainbox/frontendAPI/friendListAPI/getFriendList.php";
	private String addfriendurl = 		"http://104.131.19.115/rainbox/frontendAPI/friendListAPI/addFriend.php";
	private String deletefriendurl =	"http://104.131.19.115/rainbox/frontendAPI/friendListAPI/deleteFriend.php";
	private String getalluserurl =		"http://104.131.19.115/rainbox/frontendAPI/friendListAPI/getAllUser.php";
	private String modifystreamdurl = 	"http://104.131.19.115/rainbox/frontendAPI/streamAPI/changeStream.php";
	// ArrayList that will hold the original Data
	private String UserName = "";
	private String IdNum = "";
	private Button addfriend;
	private Button deletefriend;
	private Button shareto;
	ArrayList<HashMap<String, Object>> players;
	ArrayList<friendlistinfo> users = new ArrayList<friendlistinfo>();
	ArrayList<friendlistinfo> allusers = new ArrayList<friendlistinfo>();
	LayoutInflater inflater;
	private String addtext="";
	private EditText addinput;
	private Intent addfriendintent;
	private LayoutInflater layoutInflater;
	private boolean findfriendflag = true;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		final AlertDialog.Builder  alrtDialog = new AlertDialog.Builder(Friend_list.this);
        //		final
		LayoutInflater layoutInflater = LayoutInflater.from(this);
        //	Dialog alertDialog = new AlertDialog.Builder(this)
		//friendlistbutton_share
		
        Intent Myintent = getIntent();
        addinput = new EditText(this);
        Intent addfriendintent = new Intent(this, Tanchu.class);
        UserName = Myintent.getStringExtra("username");
        IdNum = Myintent.getStringExtra("id");
        Log.e("check", UserName+":"+IdNum);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        shareto = (Button) findViewById(R.id.friendlistbutton_share);
        addfriend = (Button) findViewById(R.id.friendlistButton_addfriend);
        addinput.addTextChangedListener(addtextlistener);
        deletefriend= (Button) findViewById(R.id.friendlistbutton_deletefriend);
        shareto= (Button) findViewById(R.id.friendlistbutton_share);
        try {
            Log.e("sendrequest", "start");
            sendrequest(IdNum,null,friendlisturl,1);
            Log.e("sendrequest", "end");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // sendrequest();
        
        //get the LayoutInflater for inflating the customomView
        //this will be used in the custom adapter
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //these arrays are just the data that
        //I'll be using to populate the ArrayList
        //You can use our own methods to get the data
        Integer[] photos={R.drawable.beijing, R.drawable.beijing2, R.drawable.beijing2, R.drawable.beijing2, R.drawable.beijing2};
        
        players=new ArrayList<HashMap<String,Object>>();
        
        //temporary HashMap for populating the
        //Items in the ListView
        HashMap<String , Object> temp;
        
        //total number of rows in the ListView
        int noOfPlayers=users.size();
        Log.v("friend number",noOfPlayers+"");
        if(users.isEmpty()){}
        else{
            Log.v("friend 1:",users.get(0).getfirstname());
            //now populate the ArrayList players
            for(int i=0;i<noOfPlayers;i++)
            {
                temp=new HashMap<String, Object>();
                
                temp.put("name", users.get(i).getlastname()+" "+users.get(i).getfirstname());
                temp.put("team", users.get(i).getuserid());
                temp.put("photo", photos[i]);
                Log.v("user"+i, users.get(i).getlastname()+users.get(i).getfirstname()+" :"+users.get(i).getuserid());
                //add the row to the ArrayList
                players.add(temp);
            }
        }
        /*create the adapter
         *first param-the context
         *second param-the id of the layout file
         you will be using to fill a row
         *third param-the set of values that
         will populate the ListView */
        adapter =new CustomAdapter(this, R.layout.list_item,players);
        //		 View tanchuview = layoutInflater.inflate(R.layout.activity_tanchu, null);
        //finally,set the adapter to the default ListView
        setListAdapter(adapter);
        addfriend.setOnClickListener(new OnClickListener(){
            @Override
            
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //			addfriendintent.putExtra("userid", IdNum);
                //			startActivity(addfriendintent);
                if (addinput != null) {
                    ViewGroup parent = (ViewGroup) addinput.getParent();
                    if (parent != null) {
                        parent.removeView(addinput);
                    }
                }
                Log.v("add","enter add");
                
                
                alrtDialog.setTitle("Add Friend")
                .setIcon(R.drawable.ic_launcher)
                .setView(addinput)
                .setPositiveButton("Add",
                                   new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        
                        
                        
                        Log.e("tanchu",alrtDialog.getContext().toString()+"  "+addtext);
                        // TODO Auto-generated method stub
                        try {
                            sendrequest(IdNum,addtext,getalluserurl,5);
                            //sendrequest(IdNum,addtext,addfriendurl,3);
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
                //alrtDialog.show();
                AlertDialog alert = alrtDialog.create();
                if(!findfriendflag){alert.cancel();}
                alert.show();
                
            }
            
        });
        shareto.setOnClickListener(new OnClickListener(){
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //			  Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
                HashMap<Integer, Boolean> state =adapter.checkBoxState;
                String options="selected:";
                String id="";
                for(int j=0;j<10;j++){
				  	System.out.println("state.get("+j+")=="+state.get(j)+"  "+state.toString());
				  	if(state.get(j)!=null){
                        HashMap<String, Object> map=(HashMap<String, Object>) adapter.getItem(j);
                        String username=map.get("name").toString()+"";
                        id=map.get("team").toString()+"";
                        options+="\n"+id+"."+username;
				  	}
                }
                //显示选择内容
                Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
                Log.e("------", "before update");
                try {
                    sendrequest(IdNum,id,modifystreamdurl,4);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("------", "after update");
                
            }
            
        });
        deletefriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<Integer, Boolean> state =adapter.checkBoxState;
                String options="selected:";
                String id="";
                for(int j=0;j<10;j++){
				  	System.out.println("state.get("+j+")=="+state.get(j)+"  "+state.toString());
				  	if(state.get(j)!=null){
                        HashMap<String, Object> map=(HashMap<String, Object>) adapter.getItem(j);
                        String username=map.get("name").toString()+"";
                        id=map.get("team").toString()+"";
                        options+="\n"+id+"."+username;
				  	}
                }
                //显示选择内容
                Toast.makeText(getApplicationContext(), options, Toast.LENGTH_LONG).show();
                Log.e("------", "before update");
                try {
                    sendrequest(IdNum,id,deletefriendurl,2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("------", "after update");
            }
	 		
	  	});
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
    addtext = s.toString()+"";
}
};
// define your custom adapter
private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>> {
    // boolean array for storing
    // the state of each CheckBox
    HashMap<Integer, Boolean> checkBoxState;
    
    ViewHolder viewHolder;
    
    public CustomAdapter(Context context, int textViewResourceId,
                         ArrayList<HashMap<String, Object>> players) {
        
        // let android do the initializing :)
        super(context, textViewResourceId, players);
        
        // create the boolean array with
        // initial state as false
        checkBoxState = new HashMap<Integer, Boolean>();
    }
    
    // class for caching the views in a row
    private class ViewHolder {
        ImageView photo;
        TextView name, team;
        CheckBox checkBox;
    }
    
    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent,false);
            viewHolder = new ViewHolder();
            
            // cache the views
            viewHolder.photo = (ImageView) convertView
            .findViewById(R.id.photo);
            viewHolder.name = (TextView) convertView
            .findViewById(R.id.name);
            viewHolder.team = (TextView) convertView
            .findViewById(R.id.team);
            viewHolder.checkBox = (CheckBox) convertView
            .findViewById(R.id.checkBox);
            
            // link the cached views to the convertview
            convertView.setTag(viewHolder);
            
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        
        int photoId = (Integer) players.get(position).get("photo");
        
        // set the data to be displayed
        viewHolder.photo.setImageDrawable(getResources().getDrawable(
                                                                     photoId));
        viewHolder.name.setText(players.get(position).get("name")
                                .toString());
        viewHolder.team.setText(players.get(position).get("team")
                                .toString());
        
        // VITAL PART!!! Set the state of the
        // CheckBox using the boolean array
        
        
        // for managing the state of the boolean
        // array according to the state of the
        // CheckBox
        
        viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    checkBoxState.put(position, isChecked);
                } else {
                    checkBoxState.remove(position);
                }
            }
        });
        viewHolder.checkBox.setChecked((checkBoxState.get(position)) == null ? false : true);
        
        // return the view to be displayed
        return convertView;
    }
    
}
// model: 1:connect  2:delete   3:add   4:share
private int sendrequest(String userId,String friendID, String url, int model) throws IOException {
InputStream result = null;
URL request = new URL(url);
Log.v("send", "start");
Map<String, Object> params = new LinkedHashMap<>();
if(model==1){
params.put("userId", userId);}
else if(model==2){
params.put("userId", userId);
params.put("friendId", friendID);
}
else if(model==3){
params.put("userId", userId);
params.put("friendId", friendID);
}
else if(model==4){
params.put("userId", userId);
params.put("streamOwnerId", friendID);
Log.v("share", "userId: "+userId);
Log.v("share", "streamOwnerId: "+friendID);
}
else if(model==5){

}
// params.put("reply_to_thread", 10394);
// params.put("password", password);
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
Log.e("connect1", conn.getContent().toString());
Log.e("connect1", result.toString());
// String encoding = conn.getContentEncoding();
// encoding = encoding == null ? "UTF-8" : encoding;
// String body =encoding.toString();
// String body = IOUtils.toString(result, encoding);
String temp = readIt(result, 500);
Log.v("connect1", temp);
// checksconnectstatus(temp);
// Reader in = new Bu÷feredReader(new
// InputStreamReader(conn.getInputStream(), "UTF-8"));
// for (int c; (c = in.read()) >= 0; System.out.print((char)c));

if(model==1){users = getfriendlist(temp);
if(users.isEmpty()||users==null){Log.e("num of friends", "no friends");}
else{Log.e("num of friends", users.size() + "");}
// Toast.makeText(this, "Your post was successfully uploaded",
// Toast.LENGTH_LONG).show();
Log.v("send", "end");}
else if(model==2){
deleteupdatelist(2,friendID);
Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
}
else if(model==3){
Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
//sendrequest(IdNum, "", addfriendurl, 3);
//			addinput.remo;

}
else if(model==4){
Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
}
else if(model==5){
//			findViewById(R.class.)
//AlertDialog.Builder  retan = new AlertDialog.Builder(Friestrend_list.this);

allusers =getalluserlist(temp);
String friendid = findfriend(allusers,friendID);
Log.v("find friend",friendid+"");
if(friendid.equals("error")){
Toast.makeText(this, "cannot find friend, Please Re-Enter email", Toast.LENGTH_LONG).show();
findfriendflag = false;
}
else {
sendrequest(IdNum,friendid,addfriendurl,3);
}

}
else{
users = getfriendlist(temp);
Log.e("num of friends", users.size() + "");
// Toast.makeText(this, "Your post was successfully uploaded",
// Toast.LENGTH_LONG).show();
Log.v("send", "end");
}
checksconnectstatus(temp);

return 1;
}
public String findfriend(ArrayList<friendlistinfo> arr, String email){
String id="error";
Log.v("find friend", email);
for(int i=0;i<arr.size();i++){
if(arr.get(i).getemail().equals(email)){
id =arr.get(i).getuserid();
}
}
return id;
}
public int addupdatelist(int model, String friendid){
int temp=0;
int result=0;
Log.e("------", "during update");
boolean status=false;
for(int i=0;i<users.size();i++){
if(players.get(i).get("team").equals(friendid)){
temp=i;
status=true;
}
}
Log.e("------", "after for loop");
if(status){result=0;}
adapter =new CustomAdapter(this, R.layout.list_item,players);

//finally,set the adapter to the default ListView
setListAdapter(adapter);
Log.e("delete", "success"+players.size());
return result;
}

public int deleteupdatelist(int model, String friendid){
int temp=0;
Log.e("------", "during update");
boolean status=false;
for(int i=0;i<users.size();i++){
if(players.get(i).get("team").equals(friendid)){
temp=i;
status=true;
}
}
Log.e("------", "after for loop");
if(status){players.remove(temp);users.remove(temp);}
adapter =new CustomAdapter(this, R.layout.list_item,players);

//finally,set the adapter to the default ListView
setListAdapter(adapter);
Log.e("delete", "success"+players.size());
return 0;
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

private int checksconnectstatus(String str) {
// int length = str.length();

// str.trim();
Log.e("check", str);
String temp = "retFlag";
int i = str.indexOf(temp);
int retflag = 100;
String ref = "";
ref = str.charAt(i + 9) + "";
if (str.contains(temp)) {
retflag = Integer.valueOf(str.charAt(i + 9) + "");
Log.v("check",
"find retflag" + "   " + i + "   " + str.charAt(i + 9)
+ "  " + "   " + retflag + "       " + ref);

} else {
Log.v("check", "lao yu, ni daye de you you cuo!");
}
// char[] temp = {'c','s'};

return retflag;
}

private ArrayList<friendlistinfo> getfriendlist(String str) {
String firstname = "firstName";
ArrayList<friendlistinfo> temp = new ArrayList<friendlistinfo>();
if(str==null||str.isEmpty()||str.indexOf("]")<=2){Log.e("friendlist","isempty");return temp;}
Log.e("friendlist",""+str.length());
int start = str.indexOf("{");
int firstnamenum;
int lastnamenum;
int useridnum;
int end = str.indexOf("}");
int last = str.indexOf("]");
String error = "null";
String lastname = "lastName";
String userid = "userId";
String firstnametxt;
String lastnametxt;
String useridtex;
boolean flag = true;
int errornum = str.indexOf(error, start);
//while(errornum!=)
Log.e("error", errornum+" "+last+"  "+str.indexOf(error, errornum+1)+"  "+str.indexOf(error, errornum+6));
while(errornum!=str.indexOf(error, errornum+1)&&(errornum!=last-4)){
Log.e("error","loop");
if(str.indexOf(error, errornum+1)==-1){break;}
errornum=str.indexOf(error, errornum+1);
}
Log.e("error",""+errornum);
while (end != (last - 1)||flag) {

if(errornum==last-4 &&(errornum==end+2)){break;}
firstnamenum = str.indexOf(firstname, start);
lastnamenum = str.indexOf(lastname, start);
useridnum = str.indexOf(userid, start);
end = str.indexOf("}", start);
firstnametxt = str.substring(firstnamenum + firstname.length() + 3,
lastnamenum - 3);
lastnametxt = str.substring(lastnamenum + lastname.length() + 3,
useridnum - 3);
useridtex = str.substring(useridnum + userid.length() + 3, end - 1)
+ "";
Log.v("user:",firstnametxt+" " +lastnametxt+" "+useridtex);
temp.add(new friendlistinfo(firstnametxt, lastnametxt, useridtex,""));
if(str.indexOf("{", start + 1)==-1){break;}
start = str.indexOf("{", start + 1);
flag = false;
}
Log.e("friend test",""+temp.toString());
return temp;
}
private ArrayList<friendlistinfo> getalluserlist(String str) {
String firstname = "firstName";
ArrayList<friendlistinfo> temp = new ArrayList<friendlistinfo>();
if(str==null||str.isEmpty()||str.indexOf("]")<=2){Log.e("friendlist","isempty");return temp;}
Log.e("friendlist",""+str.length());
int start = str.indexOf("{");
int firstnamenum;
int lastnamenum;
int useridnum;
int end = str.indexOf("}");
int last = str.indexOf("]");
String error = "null";
String lastname = "lastName";
String userid = "id";
String email = "email";
String firstnametxt;
String lastnametxt;
String useridtex;
String emailtext;
int emailnum ;
boolean flag = true;
int errornum = str.indexOf(error, start);
//while(errornum!=)
Log.e("error", errornum+" "+last+"  "+str.indexOf(error, errornum+1)+"  "+str.indexOf(error, errornum+6));
while(errornum!=str.indexOf(error, errornum+1)&&(errornum!=last-4)){
Log.e("error","loop");
if(str.indexOf(error, errornum+1)==-1){break;}
errornum=str.indexOf(error, errornum+1);
}
Log.e("error",""+errornum);
while (end != (last - 1)||flag) {

if(errornum==last-4 &&(errornum==end+2)){break;}
emailnum =  str.indexOf(email,start);
firstnamenum = str.indexOf(firstname, start);
lastnamenum = str.indexOf(lastname, start);
useridnum = str.indexOf(userid, start);
end = str.indexOf("}", start);
useridtex = str.substring(useridnum + userid.length() + 3, firstnamenum - 3)
+ "";
firstnametxt = str.substring(firstnamenum + firstname.length() + 3,
lastnamenum - 3);
lastnametxt = str.substring(lastnamenum + lastname.length() + 3,
emailnum - 3);

emailtext = str.substring(emailnum + email.length() + 3, end - 1)
+ "";
Log.v("user:",firstnametxt+" " +lastnametxt+" "+useridtex+" "+emailtext);
temp.add(new friendlistinfo(firstnametxt, lastnametxt, useridtex,emailtext));
if(str.indexOf("{", start + 1)==-1){break;}
start = str.indexOf("{", start + 1);
flag = false;
}
Log.e("friend test",""+temp.toString());
return temp;
}
}
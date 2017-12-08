package com.example.test;

public class friendlistinfo {
	private String firstname;
	private String lastname;
	private String userid;
	private boolean empty = true;
	friendlistinfo(String firstname, String lastname, String userid){
		this.firstname = firstname;
		this.lastname = lastname;
		this.userid = userid;
		empty = false;
		System.out.println(userid+" "+firstname+" "+lastname);
	}
	public String getfirstname(){
		return firstname;
	}
	public String getlastname(){
		return lastname;
	}
	public String getuserid(){
		return userid;
	}
	public boolean isEmpty(){
		return empty;
	}
	
	
}

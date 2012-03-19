package com.bridginggoodbiz;

import java.io.Serializable;

/**
 * 
 * @author jslim
 * Only username and password are encrypted!
 */
public class Business implements Serializable{
	private static final long serialVersionUID = -1389775502329725462L;
	private static String mBizId, mBizUsername, mBizPassword;
	private static String mBizName;
	
	public static void init(String bizId, String username, String password, String name){
		setBizId(bizId);
		setBizUsername(username);
		setBizPassword(password);
		setBizName(name);
	}
	
	public static void init(){
		setBizId(null);
		setBizName(null);
		setBizPassword(null);
		setBizUsername(null);
	}
	
	public static String getBizUsername(){
		return mBizUsername;
	}
	
	public static void setBizUsername(String username){
		Business.mBizUsername = username;
	}

	public static String getBizPassword() {
		return mBizPassword;
	}

	public static void setBizPassword(String mBizPassword) {
		Business.mBizPassword = mBizPassword;
	}

	public static String getBizId() {
		return mBizId;
	}

	public static void setBizId(String mBizId) {
		Business.mBizId = mBizId;
	}

	public static String getBizName() {
		return mBizName;
	}

	public static void setBizName(String mBizName) {
		Business.mBizName = mBizName;
	}
	
}

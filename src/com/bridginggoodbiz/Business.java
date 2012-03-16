package com.bridginggoodbiz;

import java.io.Serializable;

public class Business implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1389775502329725462L;
	String mEncBizId = "", mEncBizPassword= "";
	
	public Business(String encBizId, String encBizPassword){
		this.mEncBizId = encBizId;
		this.mEncBizPassword = encBizPassword;
	}
	
	public String getEncBizId(){
		return this.mEncBizId;
	}
	
	public String getEncBizPassword(){
		return this.mEncBizPassword;
	}
}

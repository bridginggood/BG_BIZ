package com.bridginggoodbiz.Stats;

public class DataRecord {
	private String mDate;
	private String mScans;
	private String mTotal;

	public DataRecord(String date, String scans, String total){
		mDate = date;
		mScans = scans;
		mTotal = total;
	}
	public String getDate() {
		return mDate;
	}
	public void setDate(String mDate) {
		this.mDate = mDate;
	}
	public String getScans() {
		return mScans;
	}
	public void setScans(String mScans) {
		this.mScans = mScans;
	}
	public String getTotal() {
		return mTotal;
	}
	public void setTotal(String mTotal) {
		this.mTotal = mTotal;
	}


}

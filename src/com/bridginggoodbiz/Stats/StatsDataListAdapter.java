package com.bridginggoodbiz.Stats;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bridginggoodbiz.R;

public class StatsDataListAdapter extends ArrayAdapter<DataRecord>{

	Activity mActivity;
	int mLayoutResourceId;    
	ArrayList<DataRecord> mData = null;

	public StatsDataListAdapter(Activity activity, int layoutResourceId, ArrayList<DataRecord> data) {
		super(activity, layoutResourceId, data);
		this.mLayoutResourceId = layoutResourceId;
		this.mData = data;
		this.mActivity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		DataRecordHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = mActivity.getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);

			//create temporary holder
			holder = new DataRecordHolder();
			holder.setDateTextView((TextView)row.findViewById(R.id.stats_datalist_date_textview));
			holder.setScansTextView((TextView)row.findViewById(R.id.stats_datalist_scan_textview));
			holder.setTotalTextView((TextView)row.findViewById(R.id.stats_datalist_total_textview));
			row.setTag(holder);
		}
		else
		{
			holder = (DataRecordHolder)row.getTag();

			final DataRecord biz = mData.get(position);
			if(biz != null){
				holder.updateDateTextView(biz.getDate());
				holder.updateScansTextView(biz.getScans());
				holder.updateTotalTextView(biz.getTotal());
			}
		}

		//Get current bizCell
		DataRecord vpCell = mData.get(position);

		//Update the content of the cell
		holder.updateDateTextView(vpCell.getDate());
		holder.updateScansTextView(vpCell.getScans());
		holder.updateTotalTextView(vpCell.getTotal());

		return row;
	}


	//Use this holder class for optimization.
	static class DataRecordHolder
	{
		TextView txtDate, txtTotal, txtScans;

		public void setDateTextView(TextView txtDate){
			this.txtDate = txtDate;
		}
		public void setTotalTextView(TextView txtTotal){
			this.txtTotal = txtTotal;
		}

		public void setScansTextView(TextView txtScans){
			this.txtScans = txtScans;
		}

		public void updateDateTextView(String txtDate){
			this.txtDate.setText(txtDate);
		}

		public void updateTotalTextView(String txtTotal){
			this.txtTotal.setText(txtTotal);
		}
		public void updateScansTextView(String txtScans){
			this.txtScans.setText(txtScans);
		}
	}
}
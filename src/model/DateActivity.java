package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateActivity {
	
	private Date startActivity;
	private Date endActivity;

	public DateActivity(){}

	public DateActivity(Date start, Date end){
		this.startActivity = start;
		this.endActivity = end;
	}
	
	public Date getStartActivity() {
		return startActivity;
	}
	public void setStartActivity(Date startActivity) {
		this.startActivity = startActivity;
	}
	public Date getEndActivity() {
		return endActivity;
	}
	public void setEndActivity(Date endActivity) {
		this.endActivity = endActivity;
	}
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  "..............." + sdf.format(startActivity) + sdf.format(endActivity);
	}
	public String toValidString(Date s) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
		System.out.println(sdf.format(s));
		return sdf.format(s);
	}
	
}

package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Forecastday implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("date")
	public String date;
	
	@SerializedName("date_epoch")
	public int date_epoch;
	
	@SerializedName("day")
	Day day = new Day();
	
	@SerializedName("astro")
	Astro astro = new Astro();
	
	@SerializedName("hour")
	ArrayList<Hour> hour = new ArrayList<Hour>();
	
    public String getDate()
    {
    	return date;
    }
    public void setDate(String mDate)
    {
    	this.date = mDate;
    }
    
    public int getDateEpoch()
    {
    	return date_epoch;
    }
    public void setDateEpoch(int mDateEpoch)
    {
    	this.date_epoch = mDateEpoch;
    }
    
    public Day getDay()
    {
    	return day;
    }
    public void setDay(Day mDay)
    {
    	this.day = mDay;
    }
    
    public Astro getAstro()
    {
    	return astro;
    }
    public void setAstro(Astro mAstro)
    {
    	this.astro = mAstro;
    }
    
    public ArrayList<Hour> getHour()
    {
    	return hour;
    }
    public void setHour(ArrayList<Hour> mHour)
    {
    	this.hour = mHour;
    }
}

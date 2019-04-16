package com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Forecastday implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("date")
    private Date date;
	
	@SerializedName("date_epoch")
    private int date_epoch;
	
	@SerializedName("day")
    private Day day = new Day();
	
	@SerializedName("astro")
    private Astro astro = new Astro();
	
	@SerializedName("hour")
    private ArrayList<Hour> hour = new ArrayList<Hour>();
	
    public Date getDate()
    {
    	return date;
    }
    public void setDate(Date mDate)
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

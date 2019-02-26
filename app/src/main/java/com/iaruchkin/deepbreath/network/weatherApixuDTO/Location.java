package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Location implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("region")
	public String region;
	
	@SerializedName("country")
	public String country;
	
	@SerializedName("tz_id")
	public String tz_id;
	
	@SerializedName("localtime")
	public String localtime;
	
	@SerializedName("lat")
	public double lat;
	
	@SerializedName("lon")
	public double lon;
	
	@SerializedName("localtime_epoch")
	public int localtime_epoch;
	
    public String getName()
    {
    	return name;
    }
    public void setName(String mName)
    {
    	this.name = mName;
    }
    
    public String getRegion()
    {
    	return region;
    }
    public void setRegion(String mRegion)
    {
    	this.region = mRegion;
    }
    
    public String getCountry()
    {
    	return country;
    }
    public void setCountry(String mCountry)
    {
    	this.country = mCountry;
    }
    
    public double getLat()
    {
    	return lat;
    }
    public void setLat(double mLat)
    {
    	this.lat = mLat;
    }
    
    public double getLong()
    {
    	return lon;
    }
    public void setLong(double mLong)
    {
    	this.lon = mLong;
    }
    
    public String getTzId()
    {
    	return tz_id;
    }
    public void setTzId(String mTz_id)
    {
    	this.tz_id = mTz_id;
    }
    
    public int getLocaltimeEpoch()
    {
    	return localtime_epoch;
    }
    public void setLocaltimeEpoch(int mLocaltimeEpoch)
    {
    	this.localtime_epoch = mLocaltimeEpoch;
    }
    
    public String getLocaltime()
    {
    	return localtime;
    }
    public void setLocaltimeEpoch(String mLocaltime)
    {
    	this.localtime = mLocaltime;
    }
    
}

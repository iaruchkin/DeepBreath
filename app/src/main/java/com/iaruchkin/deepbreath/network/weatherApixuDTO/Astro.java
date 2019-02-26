package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Astro implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("sunrise")
	public String sunrise;
	
	@SerializedName("sunset")
	public String sunset;
	
	@SerializedName("moonrise")
	public String moonrise;
	
	@SerializedName("moonset")
	public String moonset;

    public String getSunrise()
    {
    	return sunrise;
    }
    public void setSunrise(String mSunrise)
    {
    	this.sunrise = mSunrise;
    }
    
    public String getSunset()
    {
    	return sunset;
    }
    public void setSunset(String mSunset)
    {
    	this.sunset = mSunset;
    }
    
    public String getMoonrise()
    {
    	return moonrise;
    }
    public void setMoonrise(String mMoonrise)
    {
    	this.moonrise = mMoonrise;
    }
    
    public String getMoonset()
    {
    	return moonset;
    }
    public void setMoonset(String mMoonset)
    {
    	this.moonset = mMoonset;
    }
}

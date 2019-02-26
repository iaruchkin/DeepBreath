package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class WeatherModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("location")
	Location location;
	
	@SerializedName("current")
	Current current;
	
	@SerializedName("forecast")
	Forecast forecast;
	
     public Location getLocation()
     {
    	 return location;
     }
     public void setLocation(Location mLocation)
     {
    	 this.location = mLocation;
     }
     
     public Current getCurrent()
     {
    	 return current;
     }
     public void setCurrent(Current mCurrent)
     {
    	 this.current = mCurrent;
     }
     
     public Forecast getForecast()
     {
    	 return forecast;
     }
     public void setForecast(Forecast mForecast)
     {
    	 this.forecast = mForecast;
     }

}

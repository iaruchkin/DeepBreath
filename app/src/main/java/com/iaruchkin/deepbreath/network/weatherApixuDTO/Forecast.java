package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Forecast implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("forecastday")
	private ArrayList<Forecastday> forecastday = new ArrayList<Forecastday>();
	
	public ArrayList<Forecastday> getForecastday()
    {
    	return forecastday;
    }
    public void setForecastday(ArrayList<Forecastday> mForecastday)
    {
    	this.forecastday = mForecastday;
    }
}

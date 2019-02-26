package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Day implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("maxtemp_c")
	public double maxtemp_c;
	
	@SerializedName("maxtemp_f")
	public double maxtemp_f;
	
	@SerializedName("mintemp_c")
	public double mintemp_c;
	
	@SerializedName("mintemp_f")
	public double mintemp_f; 
	
	@SerializedName("avgtemp_c")
	public double avgtemp_c;
	
	@SerializedName("avgtemp_f")
	public double avgtemp_f;
	
	@SerializedName("maxwind_mph")
	public double maxwind_mph;
	
	@SerializedName("maxwind_kph")
	public double maxwind_kph;
	
	@SerializedName("totalprecip_mm")
	public double totalprecip_mm;
	
	@SerializedName("totalprecip_in")
	public double totalprecip_in;
	Condition mCondition = new Condition();

    public double getMaxtempC()
    {
    	return maxtemp_c;
    }
    public void setMaxtempC(double mMaxtemp_c)
    {
    	this.maxtemp_c = mMaxtemp_c;
    }
    
    public double getMaxtempF()
    {
    	return maxtemp_f;
    }
    public void setMaxtempF(double mMaxtemp_f)
    {
    	this.maxtemp_f = mMaxtemp_f;
    }
    
    public double getMintempC()
    {
    	return mintemp_c;
    }
    public void setMintempC(double mMintemp_c)
    {
    	this.mintemp_c = mMintemp_c;
    }
    
    public double getMintempF()
    {
    	return mintemp_f;
    }
    public void setMintempF(double mMintemp_f)
    {
    	this.mintemp_f = mMintemp_f;
    }
    
    public double getAvgtempC()
    {
    	return avgtemp_c;
    }
    public void setAvgtempC(double mAvgtemp_c)
    {
    	this.avgtemp_c = mAvgtemp_c;
    }
    
    public double getAvgtempF()
    {
    	return avgtemp_f;
    }
    public void setAvgtempF(double mAvgtemp_f)
    {
    	this.avgtemp_f = mAvgtemp_f;
    }
    
    public double getMaxwindMph()
    {
    	return maxwind_mph;
    }
    public void setMaxwindMph(double mMaxwind_mph)
    {
    	this.maxwind_mph = mMaxwind_mph;
    }
    
    public double getMaxwindKph()
    {
    	return maxwind_kph;
    }
    public void setMaxwindKph(double mMaxwind_kph)
    {
    	this.maxwind_kph = mMaxwind_kph;
    }
    
    public double getTotalprecipMm()
    {
    	return totalprecip_mm;
    }
    public void setTotalprecipMm(double mTotalprecip_mm)
    {
    	this.totalprecip_mm = mTotalprecip_mm;
    }
    
    public double getTotalprecipIn()
    {
    	return totalprecip_in;
    }
    public void setTotalprecipIn(double mTotalprecip_in)
    {
    	this.totalprecip_in = mTotalprecip_in;
    }
    
    public Condition getCondition()
    {
    	return mCondition;
    }
    public void setCondition(Condition mCondition)
    {
    	this.mCondition = mCondition;
    }
    
}

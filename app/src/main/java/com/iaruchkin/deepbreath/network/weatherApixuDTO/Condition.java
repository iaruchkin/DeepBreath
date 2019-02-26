package com.iaruchkin.deepbreath.network.weatherApixuDTO;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Condition implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("text")
	public String text;
	
	@SerializedName("icon")
	public String icon;
	
	@SerializedName("code")
	public int code;
	
    public String getText()
    {
    	return text;
    }
    public void setText(String mText)
    {
    	this.text = mText;
    }
    
    public String getIcon()
    {
    	return icon;
    }
    public void setIcon(String mIcon)
    {
    	this.icon = mIcon;
    }
    
    public int getCode()
    {
    	return code;
    }
    public void setCode(int mCode)
    {
    	this.code = mCode;
    }
    
}

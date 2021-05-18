
package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AqiAvData {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("location")
    @Expose
    private LocationAv location;
    @SerializedName("current")
    @Expose
    private Current current;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocationAv getLocation() {
        return location;
    }

    public void setLocation(LocationAv location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

}

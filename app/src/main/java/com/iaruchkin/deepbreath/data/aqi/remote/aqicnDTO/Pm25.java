
package com.iaruchkin.deepbreath.data.aqi.remote.aqicnDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pm25 {

    @SerializedName("v")
    @Expose
    private Integer v;

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}

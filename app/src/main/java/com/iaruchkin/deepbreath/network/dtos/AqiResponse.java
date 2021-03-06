
package com.iaruchkin.deepbreath.network.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.iaruchkin.deepbreath.network.dtos.aqicnDTO.AqiData;

public class AqiResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private AqiData aqiData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AqiData getAqiData() {
        return aqiData;
    }

    public void setAqiData(AqiData aqiData) {
        this.aqiData = aqiData;
    }

}

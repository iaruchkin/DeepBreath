
package com.iaruchkin.deepbreath.network.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.iaruchkin.deepbreath.network.dtos.aqiAvDTO.AqiAvData;

public class AqiAvResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose

    private AqiAvData aqiAvData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AqiAvData getAqiAvData() {
        return aqiAvData;
    }

    public void setAqiAvData(AqiAvData aqiAvData) {
        this.aqiAvData = aqiAvData;
    }

}

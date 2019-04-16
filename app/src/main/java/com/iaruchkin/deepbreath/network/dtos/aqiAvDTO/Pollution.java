
package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pollution {

    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("aqius")
    @Expose
    private Integer aqius;
    @SerializedName("mainus")
    @Expose
    private String mainus;
    @SerializedName("aqicn")
    @Expose
    private Integer aqicn;
    @SerializedName("maincn")
    @Expose
    private String maincn;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public Integer getAqius() {
        return aqius;
    }

    public void setAqius(Integer aqius) {
        this.aqius = aqius;
    }

    public String getMainus() {
        return mainus;
    }

    public void setMainus(String mainus) {
        this.mainus = mainus;
    }

    public Integer getAqicn() {
        return aqicn;
    }

    public void setAqicn(Integer aqicn) {
        this.aqicn = aqicn;
    }

    public String getMaincn() {
        return maincn;
    }

    public void setMaincn(String maincn) {
        this.maincn = maincn;
    }

}


package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("__v")
    @Expose
    private Double v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("hu")
    @Expose
    private Double hu;
    @SerializedName("ic")
    @Expose
    private String ic;
    @SerializedName("pr")
    @Expose
    private Double pr;
    @SerializedName("tp")
    @Expose
    private Double tp;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("wd")
    @Expose
    private Double wd;
    @SerializedName("ws")
    @Expose
    private Double ws;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public Double getV() {
        return v;
    }

    public void setV(Double v) {
        this.v = v;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getHu() {
        return hu;
    }

    public void setHu(Double hu) {
        this.hu = hu;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Double getPr() {
        return pr;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public Double getTp() {
        return tp;
    }

    public void setTp(Double tp) {
        this.tp = tp;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getWd() {
        return wd;
    }

    public void setWd(Double wd) {
        this.wd = wd;
    }

    public Double getWs() {
        return ws;
    }

    public void setWs(Double ws) {
        this.ws = ws;
    }

}

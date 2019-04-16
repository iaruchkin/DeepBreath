
package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("hu")
    @Expose
    private Integer hu;
    @SerializedName("ic")
    @Expose
    private String ic;
    @SerializedName("pr")
    @Expose
    private Integer pr;
    @SerializedName("tp")
    @Expose
    private Integer tp;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("ws")
    @Expose
    private Integer ws;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getHu() {
        return hu;
    }

    public void setHu(Integer hu) {
        this.hu = hu;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Integer getPr() {
        return pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public Integer getTp() {
        return tp;
    }

    public void setTp(Integer tp) {
        this.tp = tp;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getWd() {
        return wd;
    }

    public void setWd(Integer wd) {
        this.wd = wd;
    }

    public Integer getWs() {
        return ws;
    }

    public void setWs(Integer ws) {
        this.ws = ws;
    }

}

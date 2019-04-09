package com.iaruchkin.deepbreath.common;

public enum State {
    HasData,
    HasNoData,
    Loading,
    NetworkError,
    DbError,
    //    ServerError,
    Forecast,
    Current
}

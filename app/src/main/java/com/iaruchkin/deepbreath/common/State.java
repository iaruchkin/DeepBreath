package com.iaruchkin.deepbreath.common;

public enum State {
    HasData,
    HasNoData,
    Loading,
    LoadingAqi,
    NetworkError,
    DbError,
    Forecast,
    Current
}

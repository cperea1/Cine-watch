package com.example.cinewatch;

import com.example.cinewatch.Models.APIResponse;

public interface OnFetchDataListener {
    void onFetchData(APIResponse apiResponse, String message);
    void onError(String message);
}

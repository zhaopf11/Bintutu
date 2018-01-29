package com.zhurui.bunnymall.common.http;

import android.content.Context;
import android.content.Intent;

import com.zhurui.bunnymall.R;

import okhttp3.Request;
import okhttp3.Response;


/**
 */
public abstract class SimpleCallback<T> extends BaseCallback<T> {


    public SimpleCallback(Context context) {
        super(context);

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {



    }


}

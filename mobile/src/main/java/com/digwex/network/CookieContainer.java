package com.digwex.network;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieContainer implements CookieJar {
    private List<Cookie> mCookies;

    public CookieContainer() {
        mCookies = new LinkedList<>();
    }

    @Override
    public void saveFromResponse(HttpUrl url, @NonNull List<Cookie> cookies) {
        mCookies = cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return mCookies;
    }
}

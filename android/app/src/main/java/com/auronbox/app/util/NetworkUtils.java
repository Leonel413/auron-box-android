package com.auronbox.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    public static boolean temInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return true;
        Network rede = cm.getActiveNetwork();
        if (rede == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(rede);
        return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}

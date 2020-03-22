package com.lan.demo.helper;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class GlobalHelper {

    public static ApiClient getClient() throws IOException {
        return  Config.fromConfig("d://kubelet.conf");
    }
}

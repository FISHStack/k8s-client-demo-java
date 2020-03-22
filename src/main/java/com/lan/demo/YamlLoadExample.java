package com.lan.demo;

import com.lan.demo.helper.GlobalHelper;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.util.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlLoadExample {
    public static void main(String[] args) throws IOException {
        ApiClient client = GlobalHelper.getClient();
        Configuration.setDefaultApiClient(client);
        List<Object> list =  Yaml.loadAll(new File("d://deploy.yaml"));
        CoreV1Api api = new CoreV1Api();

        for (Object o : list) {
            try{
                System.out.println(o);
                if(o instanceof V1Namespace){
                    api.createNamespace((V1Namespace) o,null,null,null);
                }
                else if(o instanceof V1ServiceAccount){
                    V1ServiceAccount account = (V1ServiceAccount)o;
                    api.createNamespacedServiceAccount(account.getMetadata().getNamespace(),account,null,null,null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

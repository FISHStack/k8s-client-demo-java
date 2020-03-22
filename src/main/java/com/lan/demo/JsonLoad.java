package com.lan.demo;

import io.kubernetes.client.openapi.models.V1beta1CustomResourceDefinition;
import io.kubernetes.client.util.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonLoad {
    public static void main(String[] args) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        V1beta1CustomResourceDefinition body = yaml.loadAs(new FileReader("d://crd.yaml"), V1beta1CustomResourceDefinition.class);

    }
}

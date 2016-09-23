package com.example;

import com.example.service.DaggerServiceGraph_Component;
import com.example.service.Service;
import com.example.service.ServiceGraph;

public class DaggerMain {
    public static void main(String[] args) throws Exception {
        System.out.println(DaggerServiceGraph_Component.builder()
                                                       .serviceGraph(new ServiceGraph(new Service(), "foo"))
                                                       .build().listProductDetails().get());
    }
}

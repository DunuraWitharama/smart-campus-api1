package com.smartcampus;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import com.smartcampus.config.AppConfig;

public class Main {
    public static void main(String[] args) {
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
            URI.create("http://localhost:8080/api/v1/"),
            new AppConfig()
        );

        System.out.println("Server started at http://localhost:8080/api/v1/");
    }
}
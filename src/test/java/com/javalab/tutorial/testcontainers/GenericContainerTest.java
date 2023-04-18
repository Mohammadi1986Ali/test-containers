package com.javalab.tutorial.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericContainerTest {
    private static String methodAddress;
    private static GenericContainer simpleWebServer;

    @BeforeAll
    static void startup() {
        methodAddress = "http:";

        simpleWebServer = new GenericContainer("alpine:3.2")
                .withExposedPorts(80)
                .withCommand("/bin/sh", "-c", "while true; do echo "
                        + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");

        simpleWebServer.start();
    }

    @Test
    void givenSimpleWebServerContainer_whenGetRequest_thenReturnsResponse() throws Exception {
        String address = methodAddress
                + "//"
                + simpleWebServer.getHost()
                + ":"
                + simpleWebServer.getMappedPort(80);

        String response = simpleGetRequest(address);

        assertEquals("Hello World!", response);
    }

    private String simpleGetRequest(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    @AfterAll
    static void cleanup() {
        simpleWebServer.stop();
    }
}
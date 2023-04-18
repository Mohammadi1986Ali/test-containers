package com.javalab.tutorial.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DockerComposeContainerTest {

    private static String serviceName;
    private static int servicePort;
    private static DockerComposeContainer compose;

    @BeforeAll
    static void startup() {
        serviceName = "simpleWebServer";
        servicePort = 80;
        compose = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                .withExposedService(serviceName, servicePort);
        compose.start();
    }

    @Test
    void givenAddress_whenGetRequest_thenReturnsResponse() throws Exception {
        String address = "http:" + "//" + compose.getServiceHost(serviceName, servicePort)
                + ":" + compose.getServicePort(serviceName, servicePort);

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
        compose.stop();
    }
}

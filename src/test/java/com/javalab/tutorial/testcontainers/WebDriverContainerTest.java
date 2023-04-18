package com.javalab.tutorial.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDriverContainerTest {

    private String url;
    private String div;
    private static BrowserWebDriverContainer chrome;

    @BeforeAll
    static void startup() {
        chrome = new BrowserWebDriverContainer().withCapabilities(new ChromeOptions());
        chrome.start();
    }

    @Test
    void givenURL_whenNavigatedToPage_thenContentOfDivH1ShouldBeCorrect() {
        url = "http:" + "//" + "example.com";
        div = "/html/body/div/h1";

        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get(url);
        String heading = driver.findElement(By.xpath(div)).getText();

        assertEquals("Example Domain", heading);
    }

    @AfterAll
    static void cleanup() {
        chrome.stop();
    }
}

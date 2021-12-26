package com.test.bricktest.service;

import com.test.bricktest.configuration.ExportConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class BrickService {

    private static final Logger log = LogManager.getLogger(BrickService.class);
    private ExportConfiguration exportConfiguration;
    private static WebDriver driver = null;

    BrickService(ExportConfiguration exportConfiguration){
        this.exportConfiguration = exportConfiguration;
    }

    public void run(){
        // init driver
        init();
    }

    private void init() {
        System.setProperty("webdriver.chrome.driver", exportConfiguration.getDriverPath());
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        driver = new ChromeDriver(chromeOptions);
    }



}

package com.test.bricktest.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "export")
public class ExportConfiguration {

    private int threshold = 10;
    private String landingPageUrl;
    private String driverPath;
    private int timeoutOnClick = 10;

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public int getTimeoutOnClick() {
        return timeoutOnClick;
    }

    public void setTimeoutOnClick(int timeoutOnClick) {
        this.timeoutOnClick = timeoutOnClick;
    }
}

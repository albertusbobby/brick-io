package com.test.bricktest.service;

import com.test.bricktest.configuration.ExportConfiguration;
import com.test.bricktest.model.Product;
import com.test.bricktest.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

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

        getTopProduct();
    }

    private void init() {
        System.setProperty("webdriver.chrome.driver", exportConfiguration.getDriverPath());
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36");
        driver = new ChromeDriver(chromeOptions);
    }

    private void getTopProduct() {
        List<Product> productList = new ArrayList<>();
        int page = 1;
        do {
            driver.get(getUrlPage(page));
            List<WebElement> containers = driver.findElements(By.cssSelector("a[data-testid=lnkProductContainer]"));

            Product product = new Product();
            for (WebElement element : containers){
                WebElement name = element.findElement(By.cssSelector("span.css-1bjwylw"));
                WebElement price = element.findElement(By.cssSelector("span.css-o5uqvq"));
                WebElement store = element.findElements(By.cssSelector("span.css-1kr22w3")).get(1);
                WebElement image = element.findElement(By.cssSelector("img[crossorigin=anonymous]"));

                product.setName(name.getAttribute("innerText"));
                product.setPrice(price.getAttribute("innerText"));
                product.setMerchantName(store.getAttribute("innerText"));
                product.setImageLink(image.getAttribute("src"));

                productList.add(product);
            }

            page++;
        } while (productList.size() < exportConfiguration.getThreshold());

        CommonUtil.export(productList, exportConfiguration.getCsvPath());

    }

    private String getUrlPage(int page){
        return UriComponentsBuilder
                .fromHttpUrl(exportConfiguration.getLandingPageUrl())
                .queryParam("page", page)
                .toUriString();
    }



}

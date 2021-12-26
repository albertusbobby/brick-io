package com.test.bricktest.service;

import com.test.bricktest.configuration.ExportConfiguration;
import com.test.bricktest.model.Product;
import com.test.bricktest.util.CommonUtil;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrickService {

    private static final Logger log = LogManager.getLogger(BrickService.class);
    private static final String INNER_TEXT = "innerText";
    private static WebDriver driver = null;
    private ExportConfiguration exportConfiguration;

    BrickService(ExportConfiguration exportConfiguration) {
        this.exportConfiguration = exportConfiguration;
    }

    public void run() {
        // init driver
        init();

        // get top of products
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
        var currentWindowHandle = driver.getWindowHandle();
        int page = 1;
        do {
            driver.get(getUrlPage(page));
            List<WebElement> containers = driver.findElements(By.cssSelector("a[data-testid=lnkProductContainer]"));

            for (WebElement element : containers) {
                // get element each product
                WebElement name = element.findElement(By.cssSelector("span.css-1bjwylw"));
                WebElement price = element.findElement(By.cssSelector("span.css-o5uqvq"));
                WebElement store = element.findElements(By.cssSelector("span.css-1kr22w3")).get(1);
                WebElement image = element.findElement(By.cssSelector("img[crossorigin=anonymous]"));

                Product product = new Product();
                product.setName(name.getAttribute(INNER_TEXT));
                product.setPrice(price.getAttribute(INNER_TEXT));
                product.setMerchantName(store.getAttribute(INNER_TEXT));
                product.setImageLink(image.getAttribute("src"));

                // get detail when click product
                getProductOnClick(product, element.getAttribute("href"), currentWindowHandle);

                // add to list each product
                productList.add(product);
            }

            page++;
        } while (productList.size() < exportConfiguration.getThreshold());

        if (productList.size() > exportConfiguration.getThreshold()) {
            productList.removeIf(product -> productList.indexOf(product) >= exportConfiguration.getThreshold());
        }

        // export to csv
        CommonUtil.export(productList);

    }

    private String getUrlPage(int page) {
        return UriComponentsBuilder
                .fromHttpUrl(exportConfiguration.getLandingPageUrl())
                .queryParam("page", page)
                .toUriString();
    }

    private void getProductOnClick(Product product, String href, String currentWindowHandle) {
        try {
            var urlDestination = getUrl(decodeUrl(href));
            driver.switchTo().newWindow(WindowType.TAB);
            driver.navigate().to(urlDestination);
            log.info("Navigate to : {}", urlDestination);

            var rating = getElement("span[data-testid=lblPDPDetailProductRatingNumber]",
                    "span[data-testid=lblPDPDetailProductRatingNumber]")
                    .map(e -> e.getAttribute(INNER_TEXT))
                    .orElse("-");
            product.setRating(rating);

            var description = getElement("div[data-testid=lblPDPDescriptionProduk]",
                    "span[data-testid=lblPDPDescriptionProduk]")
                    .map(e -> e.getAttribute(INNER_TEXT))
                    .orElse("-");
            product.setDescription(description);

            driver.close();
            driver.switchTo().window(currentWindowHandle);
        } catch (Exception e) {
            String s = driver.getWindowHandle();
            if (!s.equals(currentWindowHandle)) {
                driver.close();
                driver.switchTo().window(currentWindowHandle);
                log.info("navigate to first window");
            }
        }
    }

    private Optional<WebElement> getElement(String locator, String element) {
        WebElement webElement = null;
        try {
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(exportConfiguration.getTimeoutOnClick()));
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
            webElement = driver.findElement(By.cssSelector(element));
        } catch (Exception e) {
            log.error("error while get element: {}", e.getMessage());
        }
        return Optional.ofNullable(webElement);
    }

    private String getUrl(String decodedUrl) throws URISyntaxException {
        return URLEncodedUtils.parse(new URI(decodedUrl), String.valueOf(Charset.forName("ASCII")))
                .stream()
                .filter(p -> "r".equals(p.getName()))
                .findFirst()
                .map(prm -> prm.getValue())
                .orElseThrow(() -> new RuntimeException("error when get url"));
    }

    private static String decodeUrl(String href){
        try {
            return URLDecoder.decode(href, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}

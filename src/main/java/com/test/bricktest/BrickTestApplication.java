package com.test.bricktest;

import com.test.bricktest.service.BrickService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class BrickTestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BrickTestApplication.class, args);
    }

    private BrickService brickService;

    BrickTestApplication(BrickService brickService){
        this.brickService = brickService;
    }

    @Override
    public void run(String... args) throws Exception {
//		BrickService.runApp();
        brickService.run();
    }
}

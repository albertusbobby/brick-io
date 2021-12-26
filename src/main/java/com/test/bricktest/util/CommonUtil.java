package com.test.bricktest.util;

import com.opencsv.CSVWriter;
import com.test.bricktest.model.Product;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CommonUtil {

    private static final String DELIMETER = ";";
    private static final String NAME_OF_FILE = "products.csv";

    public static void export(List<Product> products, String csvPath){
        if (products.size() == 0){
            return;
        }

        StringBuilder result = new StringBuilder();
        products.forEach(p -> {
            result.append(p.getName()).append(DELIMETER)
                    .append(p.getImageLink()).append(DELIMETER)
                    .append(p.getPrice()).append(DELIMETER)
                    .append(p.getRating()).append(DELIMETER)
                    .append(p.getMerchantName()).append(DELIMETER)
                    .append("\n");
        });

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(NAME_OF_FILE), Charset.forName("UTF-8"))){
            writer.write(result.toString());
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}

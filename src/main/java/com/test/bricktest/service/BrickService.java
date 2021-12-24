package com.test.bricktest.service;

import com.test.bricktest.configuration.ExportConfiguration;
import org.springframework.stereotype.Service;

@Service
public class BrickService {

    private ExportConfiguration exportConfiguration;

    BrickService(ExportConfiguration exportConfiguration){
        this.exportConfiguration = exportConfiguration;
    }

    public void runApp(){
        System.out.println("init running with threshold : "+exportConfiguration.getThreshold());
    }


}

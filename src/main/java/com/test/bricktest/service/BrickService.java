package com.test.bricktest.service;

import com.test.bricktest.configuration.ExportConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BrickService {

    private static final Logger log = LogManager.getLogger(BrickService.class);

    private ExportConfiguration exportConfiguration;

    BrickService(ExportConfiguration exportConfiguration){
        this.exportConfiguration = exportConfiguration;
    }

    public void run(){
        log.info("Running......");
    }


}

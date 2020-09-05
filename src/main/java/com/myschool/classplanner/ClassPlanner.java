package com.myschool.classplanner;

import com.myschool.classplanner.service.Orchestrate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassPlanner {

    public static void main(String[] args) {
        LogHelper.info("Arguments: {}", args);
        SpringApplication.run(ClassPlanner.class, args);
        Orchestrate.readFromAndWriteToDisc();
    }
}

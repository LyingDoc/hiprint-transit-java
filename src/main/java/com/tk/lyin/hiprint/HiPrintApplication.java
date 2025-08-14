package com.tk.lyin.hiprint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class HiPrintApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiPrintApplication.class, args);
        log.info("print started");
    }
}

package com.mocyx.yinwangblog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        Thread t = new Thread(new HttpServer());
        t.start();

        log.info("blog started");

        t.join();
    }


}

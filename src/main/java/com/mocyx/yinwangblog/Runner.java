package com.mocyx.yinwangblog;

import com.alibaba.fastjson.JSON;
import com.mocyx.yinwangblog.blog.BlogGenerateService;
import com.mocyx.yinwangblog.blog.GithubService;
import com.mocyx.yinwangblog.blog.entity.issue.IssuesDto;
import com.mocyx.yinwangblog.http.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class Runner implements CommandLineRunner {

    @Autowired
    GithubService githubService;

    @Autowired
    BlogGenerateService blogGenerateService;


    private void startGithubDump() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        IssuesDto issuesDto = githubService.getIssues();
                        blogGenerateService.generateBlog(issuesDto);
                        log.info("github issues copied");
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    try {
                        //每分钟刷新一次
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }

                }

            }
        });
        t.start();
    }

    @Override
    public void run(String... args) throws Exception {


        String configFile = "./config.json";
        if (args.length > 0) {
            configFile = args[0];
        }

        String str = FileUtils.readFileToString(new File(configFile), "utf-8");
        ConfigDto configDto = JSON.parseObject(str, ConfigDto.class);
        Global.config = configDto;
        //启动github更新线程
        startGithubDump();
        //启动http
        Thread t = new Thread(new HttpServer());
        t.start();
        log.info("blog started");
        t.join();
    }


}

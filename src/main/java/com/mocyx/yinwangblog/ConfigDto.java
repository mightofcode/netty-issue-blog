package com.mocyx.yinwangblog;

import com.mocyx.yinwangblog.blog.entity.LinkDto;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */


@Data
public class ConfigDto {


    private String serverIp = "0.0.0.0";
    private int serverPort = 9701;
    private String siteRoot = "http://www.yinwang.org/";

    private String blogName;

    private String githubToken;
    private String githubName;
    private String githubRepo;

    private List<LinkDto> links;
}

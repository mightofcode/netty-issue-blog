package com.mocyx.yinwangblog.blog;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mocyx.yinwangblog.BlogException;
import com.mocyx.yinwangblog.Global;
import com.mocyx.yinwangblog.blog.entity.gql.GqlQuery;
import com.mocyx.yinwangblog.blog.entity.issue.IssuesDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class GithubService {
    private final String queryStr = "{\n" +
            "  repository(owner: \"#{owner}\", name: \"#{name}\") {\n" +
            "    issues(first: 100, orderBy: {field: CREATED_AT, direction: DESC}) {\n" +
            "      nodes {\n" +
            "        author {\n" +
            "          login\n" +
            "        }\n" +
            "        bodyHTML\n" +
            "        title\n" +
            "        comments(first: 100) {\n" +
            "          nodes {\n" +
            "            bodyHTML\n" +
            "            author {\n" +
            "              login\n" +
            "            }\n" +
            "            createdAt\n" +
            "            id\n" +
            "            databaseId\n" +
            "          }\n" +
            "        }\n" +
            "        createdAt\n" +
            "            id\n" +
            "            databaseId\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private IssuesDto parseJson(String s) {
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONObject repository = (JSONObject) data.get("repository");
        JSONObject issues = (JSONObject) repository.get("issues");
        String str = JSON.toJSONString(issues);
        IssuesDto issuesDto = JSON.parseObject(str, IssuesDto.class);
        return issuesDto;
    }

    public IssuesDto getIssues() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .writeTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build();

        MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");
        GqlQuery query = new GqlQuery();

        String gql = queryStr.replace("#{owner}", Global.config.getGithubName())
                .replace("#{name}", Global.config.getGithubRepo());

        query.setQuery(gql);
        String jsonStr = JSON.toJSONString(query);
        RequestBody formBody = RequestBody.create(jsonMedia, jsonStr);

        Request request = new Request.Builder()
                .addHeader("Authorization", "bearer " + Global.config.getGithubToken())
                .url(Global.gqlEndpoint)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();

        String bodyString = IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8);

        if (!response.isSuccessful()) {
            log.error("http fail {} {}", response.code(), bodyString);
            throw new BlogException("http error");
        } else {
            log.debug("http success {} {}", response.code(), bodyString);
        }

        IssuesDto issuesDto = parseJson(bodyString);

        return issuesDto;
    }

}

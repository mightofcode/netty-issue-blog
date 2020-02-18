package com.mocyx.yinwangblog.blog;

import com.mocyx.yinwangblog.Util;
import com.mocyx.yinwangblog.Global;
import com.mocyx.yinwangblog.blog.entity.LinkDto;
import com.mocyx.yinwangblog.blog.entity.issue.IssueDto;
import com.mocyx.yinwangblog.blog.entity.issue.IssuesDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class BlogGenerateService {

    private static String indexTpl = "template/index.html";
    private static String blogTpl = "template/blog.html";
    private static String articleItemTpl = "template/articleItem.html";


    private String buildItems(IssuesDto issuesDto) throws Exception {
        String articleItemTemplate = Util.readResouce(articleItemTpl);

        StringBuilder sb = new StringBuilder();
        for (IssueDto issueDto : issuesDto.getNodes()) {

            Map<String, String> reps = new HashMap<>();

            Date date = getDate(issueDto);
            String dateString = String.format("%d年%02d月%02d日", date.getYear() + 1900, date.getMonth() + 1, date.getDate());
            reps.put("#{date}", dateString);
            String href = generateHref(issueDto);
            reps.put("#{href}", href);
            reps.put("#{title}", issueDto.getTitle());
            reps.put("#{siteRoot}", Global.config.getSiteRoot());

            String s = Util.templateReplace(articleItemTemplate, reps);
            sb.append(s);
        }
        return sb.toString();
    }

    private Date getDate(IssueDto issueDto) throws Exception {
        String tm = issueDto.getCreatedAt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = dateFormat.parse(tm);
        return date;
    }

    private String generateHref(IssueDto issueDto) throws Exception {
        Date date = getDate(issueDto);
        String href = String.format("/blog-cn/%d/%02d/%02d/%s", date.getYear() + 1900, date.getMonth() + 1, date.getDate(), issueDto.getId());
        return href;
    }

    private String generateLinks() {
        String res = "";
        for (LinkDto linkDto : Global.config.getLinks()) {
            res += String.format("<li><a href=\"%s\">%s</a></li>", linkDto.getHref(), linkDto.getTitle());
        }
        return res;
    }

    private void generateIndex(IssuesDto issuesDto) throws Exception {
        String articles = buildItems(issuesDto);
        String indexTemplate = Util.readResouce(indexTpl);
        Map<String, String> reps = new HashMap<>();
        reps.put("#{blogTitle}", Global.config.getBlogName());
        reps.put("#{articles}", articles);
        reps.put("#{siteRoot}", Global.config.getSiteRoot());

        reps.put("#{links}", generateLinks());

        String indexHtml = Util.templateReplace(indexTemplate, reps);

        String filePath = Global.webRoot + "/index.html";
        Util.deleteFile(filePath);
        writeStringToFile(indexHtml, filePath);
    }

    private void writeStringToFile(String txt, String path) throws IOException {
        Path p = Paths.get(path).toAbsolutePath();
        String s = p.getParent().toString();
        FileUtils.forceMkdir(new File(s));
        FileUtils.writeStringToFile(new File(path), txt, StandardCharsets.UTF_8);
        log.info("write file {}", path);
    }

    private void generateArticle(IssuesDto issuesDto) throws Exception {

        for (IssueDto issueDto : issuesDto.getNodes()) {
            String indexTemplate = Util.readResouce(blogTpl);
            Map<String, String> reps = new HashMap<>();
            reps.put("#{title}", issueDto.getTitle());
            reps.put("#{article}", issueDto.getBodyHTML());
            String html = Util.templateReplace(indexTemplate, reps);

            String filePath = Global.webRoot + generateHref(issueDto);
            Util.deleteFile(filePath);
            writeStringToFile(html, filePath);

        }


    }

    public void generateBlog(IssuesDto issuesDto) {
        try {
            //
            File dir = new File(Global.blogRoot);
            if (dir.exists()) {
                FileUtils.deleteDirectory(dir);
            }
            FileUtils.forceMkdir(dir);
            //
            generateIndex(issuesDto);
            //
            generateArticle(issuesDto);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }


    }


}

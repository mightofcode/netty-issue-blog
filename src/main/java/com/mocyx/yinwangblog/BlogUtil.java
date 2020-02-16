package com.mocyx.yinwangblog;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class BlogUtil {

    public static String readFile(String path) {
        try {
            return FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static void deleteFile(String path) {
        try {
            FileUtils.forceDelete(new File(path));
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
        }
    }

    public static String readResouce(String path) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(path);
            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static String templateReplace(String template, Map<String, String> reps) {
        String res = template;
        for (String k : reps.keySet()) {
            res = res.replace(k, reps.get(k));

        }
        return res;
    }


}

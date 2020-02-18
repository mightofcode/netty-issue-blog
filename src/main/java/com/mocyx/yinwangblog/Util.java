package com.mocyx.yinwangblog;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;


@Slf4j
public class Util {

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
//
//    public static byte[] readResouceAsBytes(String path) {
//        try {
//            File file = ResourceUtils.getFile("classpath:" + path);
//            return Files.readAllBytes(file.toPath());
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static String readResouce(String path) {
//        try {
//
//            File file = ResourceUtils.getFile("classpath:" + path);
//            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            StringBuilder sb = new StringBuilder();
//            for (String line : lines) {
//                sb.append(line);
//                sb.append("\n");
//            }
//            return sb.toString();
//        } catch (IOException e) {
//            return null;
//        }
//    }

    public static String templateReplace(String template, Map<String, String> reps) {
        String res = template;
        for (String k : reps.keySet()) {
            res = res.replace(k, reps.get(k));

        }
        return res;
    }


}

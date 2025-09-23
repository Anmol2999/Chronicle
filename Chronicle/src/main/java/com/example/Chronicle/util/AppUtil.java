package com.example.Chronicle.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppUtil {
    private AppUtil() {}

    // ⚠️ IMPORTANT: Change this to a REAL folder path on your machine
    private static final String UPLOAD_DIR = "D:\\Projects\\Chronicle\\Chronicle\\src\\main\\resources\\static\\uploads";

    // This method returns just the directory path
    public static String getUploadDir() {
        return UPLOAD_DIR;
    }

  
}
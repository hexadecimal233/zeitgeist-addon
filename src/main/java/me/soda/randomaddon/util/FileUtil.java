package me.soda.randomaddon.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static String read(String file) {
        return new String(read(new File(file)), StandardCharsets.UTF_8);
    }

    public static byte[] read(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}

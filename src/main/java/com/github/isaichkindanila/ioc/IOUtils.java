package com.github.isaichkindanila.ioc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class IOUtils {

    static String readAll(InputStream in) {
        try (var reader = new BufferedReader(new InputStreamReader(in))) {
            var builder = new StringBuilder();
            reader.lines().forEach(builder::append);

            return builder.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private IOUtils() {}
}

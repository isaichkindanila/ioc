package com.github.isaichkindanila.ioc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class BeanParser {

    private static String readAll(InputStream in) {
        try (var reader = new BufferedReader(new InputStreamReader(in))) {
            var builder = new StringBuilder();
            reader.lines().forEach(builder::append);

            return builder.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static BeanInfo jsonToBeanInfo(JSONObject json) {
        var className = json.getString("class");
        var args = json.getJSONArray("args").toList()
                .stream()
                .map(object -> (String) object)
                .toArray(String[]::new);

        return new BeanInfo(className, args);
    }

    static List<BeanInfo> parseBeans(InputStream in) {
        var jsonString = BeanParser.readAll(in);
        var beanArray = new JSONArray(jsonString);

        var result = new ArrayList<BeanInfo>();

        for (int i = 0; i < beanArray.length(); i++) {
            var beanJson = beanArray.getJSONObject(i);
            result.add(jsonToBeanInfo(beanJson));
        }

        return result;
    }

    private BeanParser() {}
}

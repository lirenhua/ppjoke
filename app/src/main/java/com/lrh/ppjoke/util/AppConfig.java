package com.lrh.ppjoke.util;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lrh.ppjoke.model.BottomBar;
import com.lrh.ppjoke.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by LRH on 2020/11/15 0015
 */
public class AppConfig {
    private static final String FILE_NAME = "destination.json";
    private static final String BOTTOM_FILE_NAME = "main_tabs_config.json";
    private static HashMap<String, Destination> sDestConfig;
    private static BottomBar sBottomBar;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null) {
            String content = parseFile(FILE_NAME);
            sDestConfig = JSON.parseObject(content, new TypeReference<HashMap<String, Destination>>() {
            });
        }
        return sDestConfig;
    }

    public static BottomBar getBottomBar() {
        if (sBottomBar == null) {
            String content = parseFile(BOTTOM_FILE_NAME);
            sBottomBar = JSON.parseObject(content, new TypeReference<BottomBar>() {
            });
        }
        return sBottomBar;
    }

    private static String parseFile(String fileName) {
        AssetManager assetManager = AppGlobals.getApplication().getResources().getAssets();
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            stream = assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}

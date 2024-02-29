package com.example.tradingview.SingletonFileManager;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SingletonFileManager {
    private static final String FILE_NAME = "data.json";
    private static volatile SingletonFileManager instance = null;

    private SingletonFileManager() {
    }

    public static synchronized SingletonFileManager getInstance() {
        if (instance == null)
            instance = new SingletonFileManager();
        return instance;
    }

    public void writeFile(Context context, String jsonContent) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(jsonContent.getBytes());
        fos.close();
    }

    public String readFile(Context context) throws IOException {
        FileInputStream fis = context.openFileInput(FILE_NAME);
        StringBuilder stringBuilder = new StringBuilder();
        int character;
        while ((character = fis.read()) != -1)
            stringBuilder.append((char) character);

        fis.close();
        return stringBuilder.toString();
    }

//    public void appendToFile(Context context, String newContent) throws IOException, JSONException {
//        String existingContent = readFile(context);
//        JSONArray jsonArray = new JSONArray(existingContent);
//        jsonArray.put(new JSONArray(newContent));
//        writeFile(context, jsonArray.toString());
//    }
    public void appendToFile(Context context, String dataToAppend) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
        fos.write((dataToAppend + "\n").getBytes());
        fos.close();
    }

    public void removeFromFile(Context context, int indexToRemove) throws IOException, JSONException {
        String existingContent = readFile(context);
        JSONArray jsonArray = new JSONArray(existingContent);
        if (indexToRemove < jsonArray.length()) {
            jsonArray.remove(indexToRemove);
            writeFile(context, jsonArray.toString());
        }
    }

    public void createFileIfNotExists(Context context) throws IOException {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
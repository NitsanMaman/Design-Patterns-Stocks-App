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

    public void writeFile(Context context, JSONArray jsonArray) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(jsonArray.toString().getBytes());
        fos.close();
    }

    public JSONArray readFile(Context context) throws IOException, JSONException {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists())
            createFileIfNotExists(context);

        FileInputStream fis = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fis = context.openFileInput(FILE_NAME);
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // Handle closing exception
                }
            }
        }
        // Check if the stringBuilder is empty and return an empty JSONArray if true
        if (stringBuilder.length() == 0) {
            return new JSONArray(); // Return an empty JSONArray if the file is empty
        } else {
            return new JSONArray(stringBuilder.toString()); // Parse and return the JSONArray from file content
        }
    }

    public void appendToFile(Context context, String newContent) throws IOException, JSONException {
        JSONArray jsonArray;
        try {
            jsonArray = readFile(context);
        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }
        jsonArray.put(newContent);
        writeFile(context, jsonArray);
    }

    public boolean deleteFile(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.delete();
    }

    public void removeFromFile(Context context, String symbolToRemove) throws IOException, JSONException {
        JSONArray existingArray = readFile(context);
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < existingArray.length(); i++) {
            if (!existingArray.getString(i).equals(symbolToRemove))
                newArray.put(existingArray.getString(i));
        }
        writeFile(context, newArray);
    }

    public void createFileIfNotExists(Context context) throws IOException {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists())
            file.createNewFile();
    }
}

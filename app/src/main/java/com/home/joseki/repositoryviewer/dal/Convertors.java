package com.home.joseki.repositoryviewer.dal;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.joseki.repositoryviewer.models.Parents;
import com.home.joseki.repositoryviewer.models.Weeks;

import java.lang.reflect.Type;
import java.util.List;

public class Convertors {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
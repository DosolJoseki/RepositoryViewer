package com.home.joseki.repositoryviewer.dal;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.joseki.repositoryviewer.models.Weeks;

import java.lang.reflect.Type;
import java.util.List;

public class ConvertorsWeeks {

    @TypeConverter
    public static List<Weeks> fromWeeks(String value) {
        Type listType = new TypeToken<List<Weeks>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toWeeks(List<Weeks> list) {
        return new Gson().toJson(list);
    }
}

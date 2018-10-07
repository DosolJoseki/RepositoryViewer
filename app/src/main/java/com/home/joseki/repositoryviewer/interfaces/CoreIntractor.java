package com.home.joseki.repositoryviewer.interfaces;

import com.home.joseki.repositoryviewer.enums.RoomTableEnum;

import io.reactivex.Single;

public interface CoreIntractor {
    void clearRoom(RoomTableEnum table, String url);
    void setIntToPreferences(String key, int value);
    int getIntFromPreferences(String key);
    String getResourceString(int id);
    Single<Boolean> isOnline();
}

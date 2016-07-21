package com.example.deepanshu.sportscafe;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.deepanshu.sportscafe.model.Students;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deepanshu on 20/7/16.
 */

public class SharedPreference {

    public static final String PREFS_NAME = "SPORTS_CAFES";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Students> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void addFavorite(Context context, Students product) {
        List<Students> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Students>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Students product) {
        ArrayList<Students> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Students> getFavorites(Context context) {
        SharedPreferences settings;
        List<Students> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Students[] favoriteItems = gson.fromJson(jsonFavorites,
                    Students[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Students>(favorites);
        } else
            return null;

        return (ArrayList<Students>) favorites;
    }

}

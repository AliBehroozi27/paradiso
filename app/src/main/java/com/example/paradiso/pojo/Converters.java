package com.example.paradiso.pojo;

import android.arch.persistence.room.TypeConverter;

import com.example.paradiso.pojo.detailedMovie.Genre;
import com.example.paradiso.pojo.detailedMovie.ProductionCompany;
import com.example.paradiso.pojo.detailedMovie.ProductionCountry;
import com.example.paradiso.pojo.detailedMovie.SpokenLanguage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Genre> genreFromString(String value) {
        Type listType = new TypeToken<ArrayList<Genre>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String stringFromGenre(ArrayList<Genre> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<ProductionCompany> productionCompanyFromString(String value) {
        Type listType = new TypeToken<ArrayList<ProductionCompany>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String stringFromProductionCompany(ArrayList<ProductionCompany> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<ProductionCountry> productionCountryFromString(String value) {
        Type listType = new TypeToken<ArrayList<ProductionCountry>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String stringFromProductionCountry(ArrayList<ProductionCountry> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static ArrayList<SpokenLanguage> spokenLanguageFromString(String value) {
        Type listType = new TypeToken<ArrayList<SpokenLanguage>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String stringFromSpokenLanguage(ArrayList<SpokenLanguage> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

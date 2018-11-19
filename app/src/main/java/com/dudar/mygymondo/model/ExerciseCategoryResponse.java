package com.dudar.mygymondo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExerciseCategoryResponse extends BaseResponse {
    @SerializedName("results")
    private List<ExerciseCategory> categoriesList = null;

    public List<ExerciseCategory> getCategoriesList() {
        return categoriesList;
    }
}

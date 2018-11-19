package com.dudar.mygymondo.ui;

import com.dudar.mygymondo.model.ExerciseImage;
import com.dudar.mygymondo.model.ExerciseInfo;

import java.util.List;

public interface ExerciseDetailViewInterface {

    void displayExerciseInfo(ExerciseInfo exInfo);
    void displayExerciseImages(List<ExerciseImage> images);
}

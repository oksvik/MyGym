package com.dudar.mygymondo.ui;

import com.dudar.mygymondo.model.Equipment;
import com.dudar.mygymondo.model.ExerciseCategory;
import com.dudar.mygymondo.model.ExerciseWithImage;
import com.dudar.mygymondo.model.Muscle;

import java.util.List;

public interface ExerciseListViewInterface {

    void displayExercisesList(String nextPageStr, List<ExerciseWithImage> exercises);
    void displayMoreExerciseList(String nextPageStr, List<ExerciseWithImage> exercises);

    void saveCategories(List<ExerciseCategory> categories);
    void saveEquipment(List<Equipment> equipment);
    void saveMuscles(List<Muscle> muscles);

}

package com.dudar.mygymondo.network;

import com.dudar.mygymondo.model.EquipmentResponse;
import com.dudar.mygymondo.model.ExerciseCategoryResponse;
import com.dudar.mygymondo.model.ExerciseInfo;
import com.dudar.mygymondo.model.ExerciseResponse;
import com.dudar.mygymondo.model.ImageResponse;
import com.dudar.mygymondo.model.MuscleResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GymApi {

    @GET("exercise")
    Single<ExerciseResponse> getAllExercises ();

    @GET("exercise?status=2")
    Single<ExerciseResponse> getApprovedExercises(@Query("page") int page);

    @GET("exercise?status=2")
    Single<ExerciseResponse> getApprovedExercises();

    @GET("exerciseinfo/{id}")
    Single<ExerciseInfo> getExerciseInfo(@Path("id") int exerciseId);

    @GET("exerciseimage")
    Observable<ImageResponse> getExerciseImages(@Query("exercise") int exerciseId);

    @GET("exerciseimage")
    Single<ImageResponse> getExerciseDetailImages(@Query("exercise") int exerciseId);

    @GET("exercisecategory")
    Single<ExerciseCategoryResponse> getAllCategories();

    @GET("muscle")
    Single<MuscleResponse> getAllMuscles();

    @GET("equipment")
    Single<EquipmentResponse> getAllEquipment();

}

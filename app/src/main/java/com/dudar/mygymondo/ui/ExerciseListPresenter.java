package com.dudar.mygymondo.ui;

import android.util.Log;

import com.dudar.mygymondo.app.GymConst;
import com.dudar.mygymondo.model.EquipmentResponse;
import com.dudar.mygymondo.model.Exercise;
import com.dudar.mygymondo.model.ExerciseCategoryResponse;
import com.dudar.mygymondo.model.ExerciseResponse;
import com.dudar.mygymondo.model.ExerciseWithImage;
import com.dudar.mygymondo.model.MuscleResponse;
import com.dudar.mygymondo.network.ApiService;
import com.dudar.mygymondo.network.GymApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class ExerciseListPresenter {
    private String TAG = ExerciseListPresenter.class.getSimpleName();

    private GymApi gymApi;
    private ExerciseListViewInterface exListView;
    private CompositeDisposable disposable = new CompositeDisposable();

    ExerciseListPresenter(ExerciseListViewInterface elv) {
        exListView = elv;
        gymApi = ApiService.getInstance().getGymApi();
    }

    void loadData() {
        getCategories();
    }

    private void getCategories() {
        disposable.add(
                gymApi.getAllCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ExerciseCategoryResponse>() {
                            @Override
                            public void onSuccess(ExerciseCategoryResponse exerciseCategoryResponse) {
                                exListView.saveCategories(exerciseCategoryResponse.getCategoriesList());
                                getMuscles();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    private void getMuscles() {
        disposable.add(
                gymApi.getAllMuscles()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MuscleResponse>() {
                            @Override
                            public void onSuccess(MuscleResponse muscleResponse) {
                                exListView.saveMuscles(muscleResponse.getMuscleList());
                                getEquipment();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    private void getEquipment() {
        disposable.add(
                gymApi.getAllEquipment()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<EquipmentResponse>() {
                            @Override
                            public void onSuccess(EquipmentResponse equipmentResponse) {
                                exListView.saveEquipment(equipmentResponse.getEquipmentList());
                                getApprovedExercises();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    private void getApprovedExercises() {
        disposable.add(
                gymApi.getApprovedExercises()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ExerciseResponse>() {
                            @Override
                            public void onSuccess(ExerciseResponse exerciseResponse) {
                                getApprovedExercisesFull(exerciseResponse, GymConst.LOAD_TYPE_DEFAULT);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    void getMoreExercises(int page) {
        disposable.add(
                gymApi.getApprovedExercises(page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ExerciseResponse>() {
                            @Override
                            public void onSuccess(ExerciseResponse exerciseResponse) {
                                getApprovedExercisesFull(exerciseResponse, GymConst.LOAD_TYPE_MORE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    private void getApprovedExercisesFull(ExerciseResponse exerciseResponse, int loadType) {

        Observable<Exercise> exerciseObservable = Observable.fromIterable(exerciseResponse.getExercisesList());


        disposable.add(exerciseObservable
                .flatMap(exercise -> gymApi.getExerciseImages(exercise.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(imgResp -> new ExerciseWithImage(exercise, imgResp.getImageList())))
                .toList()
                .subscribeWith(new DisposableSingleObserver<List<ExerciseWithImage>>() {
                    @Override
                    public void onSuccess(List<ExerciseWithImage> exerciseWithImages) {
                        if (loadType == GymConst.LOAD_TYPE_DEFAULT) {
                            exListView.displayExercisesList(exerciseResponse.getNextPage(), exerciseWithImages);
                        } else if (loadType == GymConst.LOAD_TYPE_MORE) {
                            exListView.displayMoreExerciseList(exerciseResponse.getNextPage(), exerciseWithImages);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }
                })
        );
    }

    void disposeAll() {
        disposable.dispose();
    }
}

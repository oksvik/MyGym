package com.dudar.mygymondo.ui;

import android.util.Log;

import com.dudar.mygymondo.model.ExerciseInfo;
import com.dudar.mygymondo.model.ImageResponse;
import com.dudar.mygymondo.network.ApiService;
import com.dudar.mygymondo.network.GymApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class ExerciseDetailPresenter {

    private String TAG = ExerciseDetailPresenter.class.getSimpleName();

    private GymApi gymApi;
    private ExerciseDetailViewInterface exDetailView;
    private CompositeDisposable disposable = new CompositeDisposable();

    ExerciseDetailPresenter(ExerciseDetailViewInterface edv) {
        exDetailView = edv;
        gymApi = ApiService.getInstance().getGymApi();
    }

    void getExerciseFullInfo(int exerciseId) {
        disposable.add(
                gymApi.getExerciseInfo(exerciseId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ExerciseInfo>() {
                            @Override
                            public void onSuccess(ExerciseInfo exerciseInfo) {
                                exDetailView.displayExerciseInfo(exerciseInfo);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        })
        );
    }

    void getExerciseAllImages(int exerciseId){
        disposable.add(
                gymApi.getExerciseDetailImages(exerciseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ImageResponse>() {
                    @Override
                    public void onSuccess(ImageResponse imageResponse) {
                        exDetailView.displayExerciseImages(imageResponse.getImageList());
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

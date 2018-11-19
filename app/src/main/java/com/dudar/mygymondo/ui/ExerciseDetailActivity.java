package com.dudar.mygymondo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.dudar.mygymondo.R;
import com.dudar.mygymondo.app.GymConst;
import com.dudar.mygymondo.model.Equipment;
import com.dudar.mygymondo.model.ExerciseImage;
import com.dudar.mygymondo.model.ExerciseInfo;
import com.dudar.mygymondo.model.Muscle;
import com.dudar.mygymondo.ui.adapter.DetailImageAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseDetailActivity extends AppCompatActivity implements ExerciseDetailViewInterface {

    int exerciseId;
    ExerciseDetailPresenter detailPresenter;

    @BindView(R.id.txtDetailExerciseName)
    TextView txtExerciseName;
    @BindView(R.id.txtDetailExerciseCategory)
    TextView txtExCategory;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.txtDetailEquipment)
    TextView txtEquipment;
    @BindView(R.id.txtDetailMuscles)
    TextView txtMuscles;
    @BindView(R.id.txtDetailSecondaryMuscles)
    TextView txtSecondaryMuscles;
    @BindView(R.id.recyclerViewImages)
    RecyclerView recyclerViewImages;

    DetailImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        ButterKnife.bind(this);

        detailPresenter = new ExerciseDetailPresenter(this);

        exerciseId = getIntent().getIntExtra(GymConst.EXTRA_EXERCISE_ID, 0);

        detailPresenter.getExerciseFullInfo(exerciseId);
        detailPresenter.getExerciseAllImages(exerciseId);
    }

    @Override
    public void displayExerciseInfo(ExerciseInfo exerciseInfo) {
        txtExerciseName.setText(exerciseInfo.getName());
        txtExCategory.setText(exerciseInfo.getCategory().getName());

        String plainText = Html.fromHtml(exerciseInfo.getDescription()).toString();
        txtDescription.setText(plainText);

        StringBuilder equipment = new StringBuilder();
        List<Equipment> equipmentList = exerciseInfo.getEquipment();
        if (!equipmentList.isEmpty()) {
            for (Equipment eq : equipmentList) {
                equipment.append(eq.getName()).append(", ");

            }
            txtEquipment.setText(equipment.toString().substring(0, equipment.length() - 2));
        }

        StringBuilder muscles = new StringBuilder();
        List<Muscle> musclesList = exerciseInfo.getMuscles();
        if (!musclesList.isEmpty()){
            for (Muscle m: musclesList){
                muscles.append(m.getName()).append(", ");
            }
            txtMuscles.setText(muscles.toString().substring(0,muscles.length()-2));
        }

        StringBuilder secMuscles = new StringBuilder();
        List<Muscle> secMusclesList = exerciseInfo.getMusclesSecondary();
        if (!secMusclesList.isEmpty()){
            for (Muscle m: secMusclesList){
                secMuscles.append(m.getName()).append(", ");
            }
            txtSecondaryMuscles.setText(secMuscles.toString().substring(0,secMuscles.length()-2));
        }

    }

    @Override
    public void displayExerciseImages(List<ExerciseImage> images) {
        if (images.isEmpty()){
            recyclerViewImages.setVisibility(View.GONE);
        }else {
            imageAdapter = new DetailImageAdapter(images);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewImages.setLayoutManager(layoutManager);
            recyclerViewImages.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
            recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewImages.setAdapter(imageAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailPresenter.disposeAll();
    }
}

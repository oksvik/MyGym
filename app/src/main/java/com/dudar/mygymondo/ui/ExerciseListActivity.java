package com.dudar.mygymondo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.dudar.mygymondo.R;
import com.dudar.mygymondo.app.GymConst;
import com.dudar.mygymondo.model.Equipment;
import com.dudar.mygymondo.model.ExerciseCategory;
import com.dudar.mygymondo.model.ExerciseWithImage;
import com.dudar.mygymondo.model.Muscle;
import com.dudar.mygymondo.ui.adapter.ExerciseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseListActivity extends AppCompatActivity implements ExerciseListViewInterface, View.OnClickListener {

    ExerciseListPresenter exListPresenter;
    ExerciseAdapter exerciseAdapter;

    @BindView(R.id.recyclerViewExercises)
    RecyclerView recyclerView;
    @BindView(R.id.progressBarListActivity)
    ProgressBar progressBar;

    List<ExerciseWithImage> exerciseWithImageList = new ArrayList<>();

    List<ExerciseCategory> allCategories = new ArrayList<>();
    List<Equipment> allEquipment = new ArrayList<>();
    List<Muscle> allMuscles = new ArrayList<>();

    int page;
    boolean nextPageAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        ButterKnife.bind(this);

        progressBar.setIndeterminate(true);

        page = 1;
        nextPageAvailable = false;

        exListPresenter = new ExerciseListPresenter(this);
        exListPresenter.loadData();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(exerciseWithImageList, recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exerciseAdapter);

        exerciseAdapter.setItemClickListener(this);

        exerciseAdapter.setOnLoadMoreListener(new ExerciseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (nextPageAvailable) {
                    page += 1;
                    exerciseWithImageList.add(null);
                    exerciseAdapter.notifyItemInserted(exerciseWithImageList.size() - 1);

                    exListPresenter.getMoreExercises(page);
                }
            }
        });
    }

    @Override
    public void displayExercisesList(String nextPageStr, List<ExerciseWithImage> exercises) {

        progressBar.setVisibility(View.INVISIBLE);
        nextPageAvailable = nextPageStr != null;

        exerciseWithImageList.addAll(exercises);

        exerciseAdapter.updateInfo(allCategories, allEquipment, allMuscles);
        exerciseAdapter.notifyDataSetChanged();

    }

    @Override
    public void displayMoreExerciseList(String nextPageStr, List<ExerciseWithImage> exercises) {
        exerciseWithImageList.remove(exerciseWithImageList.size() - 1);
        exerciseAdapter.notifyItemRemoved(exerciseWithImageList.size());

        nextPageAvailable = nextPageStr != null;

        exerciseWithImageList.addAll(exercises);
        exerciseAdapter.notifyDataSetChanged();
        exerciseAdapter.setLoaded();
    }

    @Override
    public void saveCategories(List<ExerciseCategory> categories) {
        allCategories.addAll(categories);
    }

    @Override
    public void saveEquipment(List<Equipment> equipment) {
        allEquipment.addAll(equipment);
    }

    @Override
    public void saveMuscles(List<Muscle> muscles) {
        allMuscles.addAll(muscles);
    }

    @Override
    public void onClick(View view) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
        int exerciseId = exerciseWithImageList.get(viewHolder.getAdapterPosition()).getEx().getId();

        Intent intent = new Intent(getApplicationContext(), ExerciseDetailActivity.class);
        intent.putExtra(GymConst.EXTRA_EXERCISE_ID, exerciseId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exListPresenter.disposeAll();
    }
}

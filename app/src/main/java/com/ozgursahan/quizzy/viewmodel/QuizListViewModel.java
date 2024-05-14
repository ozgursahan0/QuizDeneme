package com.ozgursahan.quizzy.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ozgursahan.quizzy.Model.QuizListModel;
import com.ozgursahan.quizzy.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.onFirestoreTaskComplete {

    private MutableLiveData<List<QuizListModel>> quizListLiveData = new MutableLiveData<>();

    private QuizListRepository repository = new QuizListRepository(this);

    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {
        return quizListLiveData;
    }

    public QuizListViewModel(){
        repository.getQuizData();
    }
    @Override
    public void quizDataLoaded(List<QuizListModel> quizListModels) {
        quizListLiveData.setValue(quizListModels);
    }

    @Override
    public void onError(Exception e) {
        Log.d("QuizERROR", "onError: " + e.getMessage());
    }
}
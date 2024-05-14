package com.ozgursahan.quizzy.views;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ozgursahan.quizzy.Model.QuizListModel;
import com.ozgursahan.quizzy.R;
import com.ozgursahan.quizzy.viewmodel.QuizListViewModel;

import java.util.List;

public class DetailFragment extends Fragment {

    private TextView title, totalQuestions;
    private Button startQuizBtn;
    private NavController navController;
    private int position;
    private QuizListViewModel viewModel;
    private ImageView topicImage;
    private String quizId;
    private long totalQueCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.detailFragmentTitle);
        totalQuestions = view.findViewById(R.id.detailFragmentQuestions);
        startQuizBtn = view.findViewById(R.id.startQuizBtn);
        topicImage =view.findViewById(R.id.detailFragmentImage);
        navController = Navigation.findNavController(view);

        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quiz = quizListModels.get(position);
                title.setText(quiz.getTitle());
                totalQuestions.setText(String.valueOf(10));
                Glide.with(view).load(quiz.getImage()).into(topicImage);

                totalQueCount = 10;
                quizId = quiz.getQuizId();
            }
        });

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragmentDirections.ActionDetailFragmentToQuizFragment action =
                        DetailFragmentDirections.actionDetailFragmentToQuizFragment();

                action.setQuizId(quizId);
                action.setTotalQueCount(totalQueCount);
                navController.navigate(action);
            }
        });
    }
}
package com.ozgursahan.quizzy.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ozgursahan.quizzy.Model.QuestionModel;
import com.ozgursahan.quizzy.R;
import com.ozgursahan.quizzy.viewmodel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private QuestionViewModel viewModel;
    private NavController navController;
    private ProgressBar progressBar;
    private Button option1Btn , option2Btn , option3Btn , nextQueBtn;
    private TextView questionTv , ansFeedBackTv , questionNumberTv , timerCountTv;
    private ImageView closeQuizBtn;
    private String quizId;
    private long totalQuestions;
    private int currentQueNo = 0;
    private boolean canAnswer = false;
    private long timer;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis; // remaining time
    private int notAnswerd = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        closeQuizBtn = view.findViewById(R.id.imageView3);
        option1Btn = view.findViewById(R.id.option1Btn);
        option2Btn = view.findViewById(R.id.option2Btn);
        option3Btn = view.findViewById(R.id.option3Btn);
        nextQueBtn = view.findViewById(R.id.nextQueBtn);
        ansFeedBackTv = view.findViewById(R.id.ansFeedbackTv);
        questionTv = view.findViewById(R.id.quizQuestionTv);
        timerCountTv = view.findViewById(R.id.countTimeQuiz);
        questionNumberTv = view.findViewById(R.id.quizQuestionsCount);
        progressBar = view.findViewById(R.id.quizCoutProgressBar);

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestions = 10;
        viewModel.setQuizId(quizId);
        viewModel.getQuestions();;

        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        nextQueBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel(); // Timer'ı durdur
                }

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Quit")
                        .setMessage("Are you sure you want to quit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                navController.navigate(R.id.action_quizFragment_to_listFragment);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startTimer(timeLeftInMillis / 1000); // Timer'ı kaldığı yerden başlat
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        loadData();
    }

    private void loadData(){
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions(){
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);

        //enable buttons , hide feedback tv , hide nextQuiz btn

        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i){
        currentQueNo = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                questionTv.setText(String.valueOf(currentQueNo) + ") " + questionModels.get(i - 1).getQuestion());
                option1Btn.setText(questionModels.get(i - 1).getOption_a());
                option2Btn.setText(questionModels.get(i - 1).getOption_b());
                option3Btn.setText(questionModels.get(i - 1).getOption_c());
                timer = questionModels.get(i-1).getTimer();
                answer = questionModels.get(i-1).getAnswer();

                //todo set current que no, to que number tv
                questionNumberTv.setText(String.valueOf(currentQueNo));
                startTimer(timer); // initial timer start
            }
        });

        canAnswer = true;
    }

    private void startTimer(long timeInSeconds){
        timeLeftInMillis = timeInSeconds * 1000;
        timerCountTv.setText(String.valueOf(timeInSeconds));
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished; // Update remaining time
                timerCountTv.setText(millisUntilFinished / 1000 + "");
                Long percent = millisUntilFinished/(timer*10);
                progressBar.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedBackTv.setText("Times Up! No answer selected");
                notAnswerd ++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQueNo == totalQuestions){
            nextQueBtn.setText("Submit");
            nextQueBtn.setEnabled(true);
            nextQueBtn.setVisibility(View.VISIBLE);
        }else{
            nextQueBtn.setVisibility(View.VISIBLE);
            nextQueBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.option1Btn:
                verifyAnswer(option1Btn);
                break;
            case R.id.option2Btn:
                verifyAnswer(option2Btn);
                break;
            case R.id.option3Btn:
                verifyAnswer(option3Btn);
                break;
            case R.id.nextQueBtn:
                if (currentQueNo == totalQuestions){
                    submitResults();
                }else{
                    currentQueNo ++;
                    loadQuestions(currentQueNo);
                    resetOptions();
                }
                break;
        }
    }

    private void resetOptions(){
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQueBtn.setVisibility(View.INVISIBLE);
        nextQueBtn.setEnabled(false);

        // Butonların arka plan rengini sıfırla
        option1Btn.setBackground(ContextCompat.getDrawable(getContext() , R.drawable.button_bg));
        option2Btn.setBackground(ContextCompat.getDrawable(getContext() , R.drawable.button_bg));
        option3Btn.setBackground(ContextCompat.getDrawable(getContext() , R.drawable.button_bg));
    }

    private void submitResults() {
        HashMap<String , Object> resultMap = new HashMap<>();
        resultMap.put("correct" , correctAnswer);
        resultMap.put("wrong" , wrongAnswer);
        resultMap.put("notAnswered" , notAnswerd);

        viewModel.addResults(resultMap);

        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);
    }

    private void verifyAnswer(Button button){
        if (canAnswer){
            if (answer.equals(button.getText())){
                button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_correct));
                correctAnswer++;
                ansFeedBackTv.setText("Correct Answer");
            }else{
                button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_wrong));
                wrongAnswer++;
                ansFeedBackTv.setText("Wrong Answer \nCorrect Answer: " + answer);
            }
        }
        canAnswer = false;
        countDownTimer.cancel();
        showNextBtn();
    }
}

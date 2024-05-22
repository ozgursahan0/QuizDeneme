package com.ozgursahan.quiz.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ozgursahan.quiz.R;
import com.ozgursahan.quiz.viewmodel.AuthViewModel;

public class SplashFragment extends Fragment {


    private AuthViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel'i oluşturur ve bağlar, bu sayede ViewModel'e erişebiliriz
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);

        // NavController'i bulur ve bağlar, bu sayede uygulama içi gezinmeyi yönetebiliriz
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handler, belirli bir süre sonra bir işlem gerçekleştirmek için kullanılır
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewModel.getCurrentUser() != null){
                    // Daha önce giriş varsa -> QUIZLIST EKRANI
                    navController.navigate(R.id.action_splashFragment_to_listFragment);
                }else{
                    // Daha önce giriş yoksa -> SIGNIN EKRANI
                    navController.navigate(R.id.action_splashFragment_to_signInFragment);
                }
            }
        }, 4000); // 4 sn

    }
}
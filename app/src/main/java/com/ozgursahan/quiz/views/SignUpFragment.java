package com.ozgursahan.quiz.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ozgursahan.quiz.R;
import com.ozgursahan.quiz.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {

    private AuthViewModel viewModel; // KULLANICI İŞLEMLERİ İÇİN, kimlik doğrulama
    private NavController navController;
    private EditText editEmail , editPass;
    private TextView signInText;
    private Button signUpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        editEmail = view.findViewById(R.id.editEmailSignUp);
        editPass = view.findViewById(R.id.editPassSignUp);
        signInText = view.findViewById(R.id.signInText);
        signUpBtn = view.findViewById(R.id.signUpBtn);

        // SIGN IN EKRANINA GÖNDERİR
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        // SIGN UP İŞLEMİ
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String pass = editPass.getText().toString();
                if (!email.isEmpty() && !pass.isEmpty()){
                    viewModel.signUp(email , pass);
                    viewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                        @Override
                        public void onChanged(FirebaseUser firebaseUser) {
                            if (firebaseUser !=null){ // BAŞARILI KAYIT
                                Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Please Enter Email & Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel'i oluşturur ve bağlar, bu sayede ViewModel'e erişebiliriz
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);

    }

}
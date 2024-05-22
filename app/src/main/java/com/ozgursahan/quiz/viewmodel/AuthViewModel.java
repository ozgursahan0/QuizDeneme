package com.ozgursahan.quiz.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ozgursahan.quiz.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    /*

    Genel Açıklama ->
        Her ViewModel, belirli bir veri kaynağı veya işlem seti için bir repository kullanır
        ve bu repository'den gelen verileri MutableLiveData aracılığıyla UI bileşenlerine iletir.
        Bu yapı, verilerin ve işlemlerin daha modüler, test edilebilir ve yönetilebilir olmasını sağlar.

        ViewModel: Android'in MVVM (Model-View-ViewModel) mimarisinde, UI verilerini tutan ve yöneten sınıflardır.
                   ViewModel sınıfları, veri katmanı ve UI katmanı arasında köprü görevi görür.

        MutableLiveData: MutableLiveData, canlı veri tutmak için kullanılan bir sınıftır.
                         Bu sınıf, veri değişikliklerini gözlemcilerle (UI bileşenleri gibi) paylaşır,
                         böylece veriler güncellendiğinde UI otomatik olarak güncellenir.

        Repository: Repository sınıfları, verileri almak, kaydetmek ve yönetmek için kullanılır.
                    Veritabanı, ağ veya başka bir veri kaynağından gelen verilere erişimi sağlar.

     */

    // AuthRepo İLE BİRLİKTE ÇALIŞIR

    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseUser currentUser;
    private AuthRepository repository;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public AuthViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthRepository(application);
        currentUser = repository.getCurrentUser();
        firebaseUserMutableLiveData = repository.getFirebaseUserMutableLiveData();
    }

    public void signUp(String email , String pass){
        repository.signUp(email, pass);
    }
    public void signIn(String email, String pass){
        repository.signIn(email, pass);
    }
    public void signOut(){
        repository.signOut();
    }
}
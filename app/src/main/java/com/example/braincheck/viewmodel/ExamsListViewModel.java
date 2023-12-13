package com.example.braincheck.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.braincheck.dao.ExamDao;
import com.example.braincheck.dao.UserDao;
import com.example.braincheck.database.AppDatabase;
import com.example.braincheck.model.Exam;
import com.example.braincheck.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExamsListViewModel extends ViewModel {


    private final MutableLiveData<List<Exam>> examsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isListEmpty = new MutableLiveData<>();


    public LiveData<List<Exam>> getExamsList() {
        return examsList;
    }

    public LiveData<Boolean> isListEmpty() {
        return isListEmpty;
    }

    public void loadExamsByUserId(Context context, Long userId, UserDao userDao) {
        userDao.getUserByUserId(userId).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Disposable disposable = Single.fromCallable(() -> {
                                AppDatabase database = AppDatabase.getDatabase(context);
                                ExamDao examDao = database.examDao();

                                // Obtenha os exames pelo ID do usuário
                                return examDao.getExamsByUserId(user.getUserId());
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.trampoline())  // Mude para o thread principal para atualizações na UI
                            .subscribe(
                                    exams -> {
                                        examsList.postValue(exams);
                                        isListEmpty.postValue(exams == null || exams.isEmpty());
                                    },
                                    throwable -> {
                                        // Trate erros aqui, se necessário
                                    }
                            );
                }
            }
        });
    }
}


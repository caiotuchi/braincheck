package com.example.braincheck.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.braincheck.dao.ExamDao;
import com.example.braincheck.database.AppDatabase;
import com.example.braincheck.model.Exam;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExamDetailsViewModel extends ViewModel {
    private ExamDao examDao;
    private final MutableLiveData<Exam> examDetails = new MutableLiveData<>();


    public void initContext(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        examDao = database.examDao();
    }

    public void updateExam(Exam exam) {
        Completable.fromAction(() -> examDao.updateExamById(
                        exam.getExamId(), exam.getName(),
                        exam.getAge(), exam.getGender(),
                        exam.getReport(), exam.getExamImage()
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.trampoline())
                .subscribe();
    }

    public void createExam(Exam exam) {
        Completable.fromAction(() -> examDao.insertExam(exam))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void loadExamById(long examId) {
        LiveData<Exam> examLiveData = examDao.getExamById(examId);

        examLiveData.observeForever(new Observer<Exam>() {
            @Override
            public void onChanged(Exam exam) {
                examDetails.postValue(exam);
                examLiveData.removeObserver(this);
            }
        });
    }

    public Completable deleteExam(long examId) {
        return Completable.fromAction(() -> examDao.deleteExamById(examId))
                .subscribeOn(Schedulers.io());
    }

    public LiveData<Exam> getExamDetails() {
        return examDetails;
    }
}

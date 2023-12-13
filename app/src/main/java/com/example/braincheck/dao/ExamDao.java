package com.example.braincheck.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.braincheck.model.Exam;

import java.util.List;

@Dao
public interface ExamDao {


    @Insert
    long insertExam(Exam exam);

    @Query("UPDATE exam SET name = :name, age = :age, gender = :gender, report = :report, examImage = :examImage WHERE examId = :examId")
    void updateExamById(long examId, String name, String age, String gender, String report, byte[] examImage);

    @Query("SELECT Exam.* FROM Exam INNER JOIN User ON Exam.userId = User.userId WHERE User.userId = :userId")
    List<Exam> getExamsByUserId(Long userId);

    @Query("SELECT * FROM Exam WHERE examId = :examId")
    LiveData<Exam> getExamById(long examId);

    @Query("DELETE FROM Exam WHERE examId = :examId")
    void deleteExamById(long examId);

}

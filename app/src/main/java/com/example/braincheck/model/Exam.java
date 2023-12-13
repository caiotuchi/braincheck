package com.example.braincheck.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE))
public class Exam {

    private long userId;
    @PrimaryKey(autoGenerate = true)
    private long examId;
    private String name;
    private String age;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    private String gender;
    private String report;
    private byte[] examImage;


    //construtor default
    public Exam(long userId, String name,
                String age, String gender,
                String report){
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.report = report;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public byte[] getExamImage() {
        return examImage;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }
    public void setExamImage(byte[] examImage) {
        this.examImage = examImage;
    }
}

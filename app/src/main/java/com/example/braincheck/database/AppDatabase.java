package com.example.braincheck.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.braincheck.dao.ExamDao;
import com.example.braincheck.dao.UserDao;
import com.example.braincheck.model.Converters.Converters;
import com.example.braincheck.model.Exam;
import com.example.braincheck.model.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Exam.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ExamDao examDao();

    private static volatile AppDatabase database;

    public static AppDatabase getDatabase(Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .setQueryExecutor(Executors.newSingleThreadExecutor())
                            .setTransactionExecutor(Executors.newSingleThreadExecutor())
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return database;
    }
}

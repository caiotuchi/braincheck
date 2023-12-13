package com.example.braincheck.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.braincheck.model.User;

@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    User loginUser(String username, String password);

    @Query("SELECT * FROM User WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE userId = :userId")
    LiveData<User> getUserByUserId(Long userId);
}
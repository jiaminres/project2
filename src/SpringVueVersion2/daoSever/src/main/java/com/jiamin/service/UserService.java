package com.jiamin.service;

import org.example.common.entity.User;
import com.jiamin.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> selectAllUsersExceptOwn(String accountID){
        return userMapper.selectAllUsersExceptOwn(accountID);
    }

    public boolean insertOneUser(User user){
        return userMapper.insertOneUser(user);
    }

    public User selectUserForLogin(String accountID,String password){return userMapper.selectUserForLogin(accountID, password);}
}

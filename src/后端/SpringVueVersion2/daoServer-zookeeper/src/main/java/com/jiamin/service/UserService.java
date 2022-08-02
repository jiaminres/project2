package com.jiamin.service;

import org.apache.ibatis.annotations.Param;
import org.example.common.entity.User;
import com.jiamin.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    public List<User> selectAllUsersExceptOwn(String userID, String groupID) {
        return userMapper.selectAllUsersExceptOwn(userID, groupID);
    }

    public boolean insertOneUser(User user) {
        return userMapper.insertOneUser(user);
    }

    public User selectUserForLogin(String userID, String password) {
        return userMapper.selectUserForLogin(userID, password);
    }

    public boolean modifyUserName(String userID, String userName) {
        return userMapper.modifyUserName(userID, userName);
    }

    public boolean modifyHeadImageAddress(String userID, String headImageAddress) {
        return userMapper.modifyHeadImageAddress(userID, headImageAddress);
    }

}

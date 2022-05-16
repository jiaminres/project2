package com.jiamin.mappers;

import org.example.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    /*查询所有用户(除了自身)，用于显示用户列表*/
    public List<User> selectAllUsersExceptOwn(@Param("accountID") String accountID);
    /*查询指定账号密码的用户，用于登录*/
    public User selectUserForLogin(@Param("accountID") String accountID,@Param("password") String password);
    /*注册账户*/
    public boolean insertOneUser(User user);

}

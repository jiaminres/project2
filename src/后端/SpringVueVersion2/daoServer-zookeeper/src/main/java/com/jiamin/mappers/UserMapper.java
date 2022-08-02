package com.jiamin.mappers;

import org.example.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    /*查询某群组所有用户(除了自身)，用于显示用户列表*/
    public List<User> selectAllUsersExceptOwn(@Param("userID") String userID, @Param("groupID")String groupID);
    /*查询指定账号密码的用户，用于登录*/
    public User selectUserForLogin(@Param("userID") String userID,@Param("password") String password);
    /*注册账户*/
    public boolean insertOneUser(User user);
    /*修改用户名*/
    public boolean modifyUserName(@Param("userID") String userID, @Param("userName") String userName);
    /*修改用户头像*/
    public boolean modifyHeadImageAddress(@Param("userID") String userID, @Param("headImageAddress") String headImageAddress);

}

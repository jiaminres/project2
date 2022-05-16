package com.jiamin.mappers;

import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Group;

import java.util.List;

public interface GroupMapper {
    //查找该用户加入的所有组
    public List<Group> selectGroup(String userID);
    //创建一个组
    public boolean insertOneGroup(Group group);
    //将用户加入指定的组
    public boolean addUserIntoGroup(@Param("userID")String userID,@Param("groupID")String groupID);
    //检索组
    public Group searchGroup(String groupID);
}

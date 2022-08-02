package com.jiamin.service;

import com.jiamin.mappers.GroupMapper;
import org.example.common.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired(required = false)
    private GroupMapper groupMapper;

    public List<Group> selectGroup(String userID) {
        return groupMapper.selectGroup(userID);
    }

    public boolean createGroup(Group group) {
        return groupMapper.insertOneGroup(group);
    }

    public boolean join(String userID, String groupID) {
        return groupMapper.addUserIntoGroup(userID, groupID);
    }

    public Group searchGroup(String groupID){return groupMapper.searchGroup(groupID);}
}

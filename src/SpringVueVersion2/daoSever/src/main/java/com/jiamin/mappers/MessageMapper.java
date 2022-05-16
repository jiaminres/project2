package com.jiamin.mappers;

import org.example.common.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    /*查询用户A和用户B的历史聊天记录*/
    public List<Message> selectMessageFromSendAndAccept(@Param("UserAID")String userAID,@Param("UserBID") String userBID);

    /*记录聊天记录*/
    public boolean insertOneMessage(Message message);
}

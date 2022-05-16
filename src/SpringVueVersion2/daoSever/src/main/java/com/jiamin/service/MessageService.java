package com.jiamin.service;

import org.example.common.entity.Message;
import com.jiamin.mappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    public List<Message> selectMessageFromSendAndAccept(String userAID,String userBID){
        return selectMessageFromSendAndAccept(userAID, userBID);
    }

    public boolean insertOneMessage(Message message){
        return messageMapper.insertOneMessage(message);
    }
}

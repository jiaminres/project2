package com.jiamin.service;

import com.jiamin.mapper.RecordMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.common.entity.Record;

import java.util.HashMap;
import java.util.Map;

public class RecordService {
    private SqlSessionFactory factory;
    public RecordService(){
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = sqlSessionFactoryBuilder.build(this.getClass().getClassLoader().getResourceAsStream("mybatis.xml"));
        this.factory = factory;
    }
    public void insertOneRecord(Record record){
        SqlSession session = factory.openSession(true);
        RecordMapper mapper = session.getMapper(RecordMapper.class);
        mapper.insertOneRecord(record);
        session.close();
    }
}

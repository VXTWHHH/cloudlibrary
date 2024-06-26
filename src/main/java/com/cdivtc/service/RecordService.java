package com.cdivtc.service;
import com.cdivtc.domain.Record;
import com.cdivtc.domain.User;
import com.cdivtc.entity.PageResult;

public interface RecordService {
    //新增借阅记录
    Integer addRecord(Record record);

    //查询借阅记录
    PageResult searchRecord(Record record, User user,Integer pageNum,Integer pageSize);
}

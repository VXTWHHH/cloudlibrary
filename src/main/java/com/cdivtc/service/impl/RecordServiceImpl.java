package com.cdivtc.service.impl;

import com.cdivtc.domain.Record;
import com.cdivtc.domain.User;
import com.cdivtc.entity.PageResult;
import com.cdivtc.mapper.RecordMapper;
import com.cdivtc.service.RecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Integer addRecord(Record record) {
        return recordMapper.addRecord(record);
    }

    /**
     * 查询借阅记录
     * @param record
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult searchRecord(Record record, User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(!"ADMIN".equals(user.getRole())){
            record.setBorrower(user.getName());
        }
        Page<Record> page = recordMapper.searchRecords(record);
        return new PageResult(page.getTotal(),page.getResult());
    }
}

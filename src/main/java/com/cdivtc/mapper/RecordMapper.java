package com.cdivtc.mapper;

import com.cdivtc.domain.Record;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface RecordMapper {
    //新增借阅记录
    Integer addRecord(Record record);

    //查询借阅记录
    @Select({
            "<script>" +
                    "select * from record where 1=1 " +
                    "<if test=\"borrower!=null\"> and record_borrower like concat('%',#{borrower},'%')</if>" +
                    " <if test=\"bookname!=null\">and record_bookname like concat('%',#{bookname},'%')</if>" +
                    "order by record_remandtime desc" +
                    "</script>"
    })
    @Results(id = "recordMap",value = {
            //id字段默认为false,表示不是主键
            //column表示数据库字段，property表示实体类字段名
            @Result(id = true,column = "record_id",property = "id"),
            @Result(column = "record_bookname",property = "bookname"),
            @Result(column = "record_bookisbn",property = "bookisbn"),
            @Result(column = "record_borrower",property = "borrower"),
            @Result(column = "record_borrowertime",property = "borrowTime"),
            @Result(column = "record_remandtime",property = "remandTime")
    })
    Page<Record> searchRecords(Record record);
}

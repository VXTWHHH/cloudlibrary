package com.cdivtc.service;

import com.cdivtc.domain.Book;
import com.cdivtc.domain.User;
import com.cdivtc.entity.PageResult;

/*
 *
 * 图书接口
 * */
public interface BookService {
    //查询最新上架的图书
    PageResult selectNewBooks(Integer pageNum, Integer pageSize);

    //查询图书信息
    PageResult search(Book book, Integer pageNum, Integer pageSize);

    //新增图书
    Integer addBook(Book book);

    //编辑图书信息
    Integer editBook(Book book);

    //查询当前借阅的图书
    PageResult searchBorrowed(Book book,User user,Integer pageNum, Integer pageSize);

    //归还图书
    boolean returnBook(String id,User user);

    //归还确认
    Integer returnConfirm(String id);

    //根据id查询图书信息
    Book findById(String id);

    //借阅图书
    Integer borrowBook(Book book);
}

package com.cdivtc.service.impl;

import com.cdivtc.domain.Book;
import com.cdivtc.domain.User;
import com.cdivtc.entity.PageResult;
import com.cdivtc.mapper.BookMapper;
import com.cdivtc.mapper.RecordMapper;
import com.cdivtc.service.BookService;
import com.cdivtc.service.RecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cdivtc.domain.Record;
//import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    //注入BookMapper对象
    @Autowired
    private BookMapper bookMapper;
    //注入RecordService对象
    @Autowired
    private RecordService recordService;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /*
    * 根据当前页码和每页需要展示的数据条数，查询最新上架的图书信息
    * @param pageNum当前页码
    * @param pageSize每页显示数量*/
    @Override
    public PageResult selectNewBooks(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        Page<Book> page=bookMapper.selectNewBooks();
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     *根据图书名称，图书作者，出版社查询结果集
     * @param book 图书类
     * @param pageNum 当前页码
     * @param PageSize 每页展示数量
     * @return PageResult对应的实体类
     */
    @Override
    public PageResult search(Book book,Integer pageNum,Integer PageSize){
        PageHelper.startPage(pageNum, PageSize);
        Page<Book> page = bookMapper.searchBooks(book);
        return new PageResult(page.getTotal(),page.getResult());
    }
    @Override
    public Integer addBook(Book book){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //设置新增图书上架时间
        book.setUploadTime(dateFormat.format(new Date()));
        return bookMapper.addBook(book);
    }

    @Override
    public Integer editBook(Book book) {
        return bookMapper.editBook(book);
    }

    @Override
    public PageResult searchBorrowed(Book book, User user, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize); // 设置分页查询的参数，开始分页
        Page<Book> page;
        book.setBorrower(user.getName()); //将当前登录的用户放入查询条件中
        //如果是管理员，查询当前用户借阅但未归还的图书和所有待确认归还的图书
        if("ADMIN".equals(user.getRole())) {
            page= bookMapper.selectBorrowed(book);
        } else {//如果是普通用户，查询当前用户借阅但未归还的图书
            page= bookMapper.selectMyBorrowed(book);	}
        return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    * 根据id查询图书信息
    * @param id 图书id
    * */
    @Override
    public Book findById(String id) {
        return bookMapper.findById(id);
    }
    /*
    * 借阅图书
    * @param book 申请借阅的图书*/
    @Override
    public Integer borrowBook(Book book) {
        //根据id查询出需要借阅的完整图书信息
        Book b = this.findById(book.getId()+"");
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        //设置当天为借阅时间
        book.setBorrowTime(dataFormat.format(new Date()));
        //设置所借阅的图书状态为借阅中
        book.setStatus("1");
        //将图书的价格设置在book对象中
        book.setPrice(b.getPrice());
        //将图书的上架设置在book对象中
        book.setUploadTime(b.getUploadTime());
        return bookMapper.editBook(book);
    }

    @Override
    public boolean returnBook(String id, User user) {
        //根据图书id查询出完整信息
        Book book = this.findById(id);
        //再次核验当前的登录人员和借阅者是否为同一人
        boolean rb = book.getBorrower().equals(user.getName());
        if(rb){
            //将图书状态修改为归还中
            book.setStatus("2");
            bookMapper.editBook(book);
        }
        return rb;
    }

    /**
     * 归还确认
     * @param id
     * @return
     */
    @Override
    public Integer returnConfirm(String id) {
        //根据图书Id查询待归还图书的完整信息
        Book book = this.findById(id);
        //根据归还确认的图书信息,设置借阅记录
        Record record = this.setRecord(book);
        //将图书的状态改为可借阅
        book.setStatus("0");
        //清除当前图书的借阅人信息
        book.setBorrower("");
        //清除借阅时间信息
        book.setBorrowTime("");
        book.setReturnTime("");
        Integer count = bookMapper.editBook(book);
        //如果归还成功，新增借阅记录
        if (count==1){
            return recordService.addRecord(record);
        }
        return 0;
    }

    private Record setRecord(Book book){
        Record record = new Record();
        //设置借阅记录的图书名称
        record.setBookname(book.getName());
        //设置借阅记录的图书isbn
        record.setBookisbn(book.getIsbn());
        //设置借阅记录的借阅人
        record.setBorrower(book.getBorrower());
        //设置借阅记录的借阅时间
        record.setBorrowTime(book.getBorrowTime());
        //设置图书归还确认的当天为图书归还时间
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        record.setRemandTime(dateFormat.format(new Date()));
        return record;
    }
}

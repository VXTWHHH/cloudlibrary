package com.cdivtc.controller;

import com.cdivtc.domain.Record;
import com.cdivtc.domain.User;
import com.cdivtc.entity.PageResult;
import com.cdivtc.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @RequestMapping("/searchRecords")
    public ModelAndView searchRecord(Record record, HttpServletRequest request,Integer pageNum,Integer pageSize){
        if(pageNum==null){
            pageNum = 1;
        }
        if(pageSize==null){
            pageSize = 10;
        }
        //获取当前登录的用户信息
        User user = (User)request.getSession().getAttribute("USER_SESSION");
        PageResult pageResult = recordService.searchRecord(record,user,pageNum,pageSize);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("record");
        modelAndView.addObject("pageResult",pageResult);
        modelAndView.addObject("search",record);
        modelAndView.addObject("pageNum",pageNum);
        modelAndView.addObject("gourl",request.getRequestURI());
        return modelAndView;
    }
}

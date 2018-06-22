package com.moonsun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 82610 on 2018/6/13.
 */
@RestController
public class LogSearchController {

    @GetMapping("/index")
    public ModelAndView index(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("user",1);
        return new ModelAndView("index",data);
    }

}

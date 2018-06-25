package com.moonsun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 82610 on 2018/6/13.
 */
@Controller
public class LogSearchController {

    @RequestMapping ("/")
    public String index(){
        return "index";
    }

}

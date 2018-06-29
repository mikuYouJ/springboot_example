package com.moonsun.controller;

import com.moonsun.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 82610 on 2018/6/13.
 */
@RestController
public class LogSearchController {


    @Autowired
    private ILogService logService;

    /**
     * 查询日志
     * @param index
     * @param content
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/api/queryLogs")
    public Map<String,Object> queryLogs(@RequestParam("index") String index, @RequestParam("content") String content,
                                        @RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime,
                                        @RequestParam("page")int page,@RequestParam("pageSize")int pageSize
                                        ){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("index",index);
        result.put("content",content);
        result.put("beginTime",beginTime);
        result.put("endTime",endTime);
        result.put("page",page);
        result.put("pageSize",pageSize);
        Map<String,Object> data = logService.queryLogs(result);
        return data;
    }
}

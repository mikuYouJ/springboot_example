package com.moonsun.controller;

import com.alibaba.fastjson.JSONObject;
import com.moonsun.service.ILogService;
import com.moonsun.vo.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by 82610 on 2018/6/13.
 */
@RestController
public class LogSearchController {


    @Autowired
    private ILogService logService;



    /**
     * 获取索引
     * @return
     */
    @RequestMapping(value  = "/api/getIndexs",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> getIndexs(){
        Map<String,Object> result = new HashMap<String,Object>();
        return result;
    }



    /**
     * 查询日志
     * @return
     */
    @RequestMapping(value  = "/api/queryLogs",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Map<String,Object> queryLogs(@RequestBody JSONObject jsonParam){
        Map<String,Object> result = new HashMap<String,Object>();
        System.out.println(jsonParam);
        result.put("index",jsonParam.get("index"));
        result.put("content",jsonParam.get("content"));
        result.put("beginTime",jsonParam.get("beginTime"));
        result.put("endTime",jsonParam.get("endTime"));
        result.put("page",jsonParam.get("page"));
        result.put("pageSize",jsonParam.get("pageSize"));
        Map<String,Object> data = logService.queryLogs(result);
        return data;
    }
}

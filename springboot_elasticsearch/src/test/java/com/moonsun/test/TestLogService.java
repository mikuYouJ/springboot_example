package com.moonsun.test;

import com.moonsun.Application;
import com.moonsun.service.ILogService;
import com.moonsun.service.impl.LogServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 82610 on 2018/6/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TestLogService {

    @Autowired
    private ILogService logService;

    @Test
    public void testQuery(){
        Map<String,Object> params = new HashMap<String,Object>();
         params.put("index","yth");
         params.put("content","szAirlinesWebSystem");
         params.put("page",1);
         params.put("pageSize",10);
         params.put("beginTime","2018-07-02 00:00:00");
         params.put("endTime","2018-07-03 23:00:00");
         logService.queryLogs(params);
    }
}

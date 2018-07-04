package com.moonsun.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Test
    public void test(){
       String jsonSrt = "{\"total\":28,\"row\":[{\"@timestamp\":\"2018-07-03T00:44:05.589Z\",\"@version\":\"1\",\"type\":\"szAirlinesWebSystem\",\"message\":\"{\\\"@source\\\":\\\"szAirlinesSystem\\\",\\\"@source_host\\\":\\\"pp-app01\\\",\\\"@source_path\\\":null,\\\"@type\\\":\\\"szAirlinesSystem\\\",\\\"@tags\\\":[\\\"szAirlinesSystem\\\"],\\\"@message\\\":\\\"==> Parameters: 118070225950(Long)\\\",\\\"@timestamp\\\":\\\"2018-07-03T08:44:04.424+0800\\\",\\\"@fields\\\":{\\\"logger\\\":\\\"com.skyecho.shenzhenairlines.order.vo.AirBookLineVO.selectByResId\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"thread\\\":\\\"http-bio-18080-exec-77\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"location\\\":{\\\"class\\\":\\\"org.apache.ibatis.logging.jdbc.BaseJdbcLogger\\\",\\\"method\\\":\\\"debug\\\",\\\"file\\\":\\\"BaseJdbcLogger.java\\\",\\\"line\\\":\\\"132\\\"}}}\"}]}";
        JSONObject jsonObject = JSON.parseObject(jsonSrt);
        JSONArray jsonArray = (JSONArray) jsonObject.get("row");
        JSONObject rowobj = (JSONObject) jsonArray.get(0);
        String messgae = (String) rowobj.get("message");
        JSONObject messgaeJO = JSON.parseObject(messgae);
        System.out.println(messgaeJO);
    }
}

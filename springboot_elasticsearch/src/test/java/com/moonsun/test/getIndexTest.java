package com.moonsun.test;

import com.moonsun.Application;
import com.moonsun.client.LinkClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 82610 on 2018/6/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class getIndexTest {

    @Autowired
    private LinkClient linkClient;

    @Test
    public void getIndexTest() {
        GetRequest request = new GetRequest("szairlineswebsystem-2018.06.01");
        try {

            RestHighLevelClient client = linkClient.buildClient();
            GetResponse response = client.get(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIndex() {
        GetRequest request = new GetRequest("twitter");
        try {
            RestHighLevelClient client = linkClient.buildClient();
            GetResponse response = client.get(request);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void QueryTest(){
        try {
            RestHighLevelClient client = linkClient.buildClient();
            SearchRequest searchRequest = new SearchRequest("logstash-nginx-access-2018.06.06");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0);//开始index
            searchSourceBuilder.size(5);//结束index

            String[] includeFields = new String[] {"message"};
            String[] excludeFields = new String[] {"_type"};
            searchSourceBuilder.fetchSource(includeFields, excludeFields);

            /**
             * 匹配查询
             */
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "");
            matchQueryBuilder.fuzziness(Fuzziness.AUTO);
            matchQueryBuilder.prefixLength(3);
            matchQueryBuilder.maxExpansions(10);
            searchSourceBuilder.query(matchQueryBuilder);

//            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}

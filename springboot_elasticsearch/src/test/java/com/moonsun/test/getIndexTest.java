package com.moonsun.test;

import com.moonsun.Application;
import com.moonsun.client.LinkClient;
import com.moonsun.utils.DateUtils;
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
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "Gecko");
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
        }finally {
            try {
                linkClient.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 前缀查询
     */
    @Test
    public void prefixQuery(){
        try {
            RestHighLevelClient client = linkClient.buildClient();
            SearchRequest searchRequest = new SearchRequest("logstash-nginx-access-2018.06.06");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0);//开始index
            searchSourceBuilder.size(5);//结束index

//            String[] includeFields = new String[] {"message"};
//            String[] excludeFields = new String[] {"_type"};
//            searchSourceBuilder.fetchSource(includeFields, excludeFields);

            PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("message","szAirlinesInterfaces");

            searchSourceBuilder.query(prefixQueryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                linkClient.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 范围查询
     */
    @Test
    public void rangeQuery(){
        try {
            RestHighLevelClient client = linkClient.buildClient();
            SearchRequest searchRequest = new SearchRequest("logstash-nginx-access-2018.06.06");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0);//开始index
            searchSourceBuilder.size(5);//结束index
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp");
            rangeQueryBuilder.from(DateUtils.getInstance().toSolrTime("2018-06-06 07:28:00"));
            rangeQueryBuilder.to(DateUtils.getInstance().toSolrTime("2018-06-06 08:28:00"));
            rangeQueryBuilder.includeLower(true);
            rangeQueryBuilder.includeUpper(false);
            searchSourceBuilder.query(rangeQueryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                linkClient.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通配符查询, 支持 *
     * 匹配任何字符序列, 包括空
     * 避免* 开始, 会检索大量内容造成效率缓慢
     */
    @Test
    public void wiidCardQuery(){
        try {
            RestHighLevelClient client = linkClient.buildClient();
            SearchRequest searchRequest = new SearchRequest("logstash-nginx-access-2018.06.06");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from(0);//开始index
            searchSourceBuilder.size(10);//结束index

            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("message","szAirlinesInterfaces");
            searchSourceBuilder.query(wildcardQueryBuilder);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                linkClient.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.moonsun.test;

import com.moonsun.Application;
import com.moonsun.client.LinkTransportClient;
import com.moonsun.utils.DateUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.management.Query;

/**
 * Created by 82610 on 2018/6/21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class transportTest {

    @Autowired
    private LinkTransportClient client;


    @Test
    public void wildcardQuery() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", "117*");//J?V*
        SearchResponse response = client.buildClient().prepareSearch("logstash-nginx-access-2018.06.06").setQuery(queryBuilder).get();
        System.out.println(response);
    }

    /**
     * +包含 -除外
     * <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.5/java-full-text-queries.html#java-query-dsl-simple-query-string-query'>
     * @throws Exception
     */
    @Test
    public void queryStringQuery() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("118*");
        SearchResponse response = client.buildClient().prepareSearch("logstash-nginx-access-2018.06.06").setFrom(0).setSize(100).setQuery(queryBuilder).get();
        for (SearchHit  searchHit: response.getHits()) {
            println(searchHit);
        }

        System.out.println(response);
    }


    /**
     * 复杂查询
     */
    @Test
    public void complexSearch1(){
        int page=1;
        int pageSize=10;
        String keyword="118070225950";
        String index = "yth-2018.07.02";

        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        if(keyword!=null&&!keyword.equals("")){
                //时间范围
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp")
                    .from(DateUtils.getInstance().toSolrTime("2018-06-06 00:00:00"))
                    .to(DateUtils.getInstance().toSolrTime("2018-06-06 23:59:00"))
                    .includeLower(true)
                    .includeUpper(false);
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(keyword);
            boolQueryBuilder.must(rangeQueryBuilder).must(queryStringQueryBuilder);
        }else{
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }
        SearchResponse response=client.buildClient().prepareSearch(index).setTypes("doc")
                .setQuery(boolQueryBuilder)
                .setFrom((page-1)*pageSize).setSize(pageSize)
                .setExplain(true)
                .get();

        System.out.println(response);
    }





    /**
     * 输出结果SearchResponse
     */
    public static void println(SearchHit searchHit){
        System.err.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.err.println(
                "docId : " + searchHit.docId() + "\n" +
                        "getId : " + searchHit.getId() + "\n" +
                        "getIndex : " + searchHit.getIndex()+ "\n" +
                        "getScore : " + searchHit.getScore() + "\n" +
                        "getSourceAsString : " + searchHit.getSourceAsString() + "\n" +
                        "getType : " + searchHit.getType() + "\n" +
                        "getVersion : " + searchHit.getVersion() + "\n" +
                        "fieldsOrNull : " + searchHit.fieldsOrNull() + "\n" +
                        "getExplanation : " + searchHit.getExplanation() + "\n" +
                        "getFields : " + searchHit.getFields() + "\n" +
                        "hasSource : " + searchHit.hasSource()
        );
    }


//    @Test
//    public void deleteAllIndex(){
//
//        String indexName="nginx-access";
//
//        /**
//         * 两种方式如下:
//         */
//
//        //1)
//        //可以根据DeleteIndexResponse对象的isAcknowledged()方法判断删除是否成功,返回值为boolean类型.
//        DeleteIndexResponse dResponse = client.buildClient().admin().indices().prepareDelete(indexName)
//                .execute().actionGet();
//        System.out.println("是否删除成功:"+dResponse.isAcknowledged());
//
//        //2)
//        //如果传人的indexName不存在会出现异常.可以先判断索引是否存在：
//        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
//
//        IndicesExistsResponse inExistsResponse = client.buildClient().admin().indices()
//                .exists(inExistsRequest).actionGet();
//
//        //根据IndicesExistsResponse对象的isExists()方法的boolean返回值可以判断索引库是否存在.
//        System.out.println("是否删除成功:"+inExistsResponse.isExists());
//    }

}

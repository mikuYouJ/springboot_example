package com.moonsun.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moonsun.client.LinkTransportClient;
import com.moonsun.service.ILogService;
import com.moonsun.utils.DateUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 82610 on 2018/6/25.
 */
@Service
public class LogServiceImpl implements ILogService {
    private final static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private LinkTransportClient client;


    /**
     * elasticsearch为了方便后期删除对应日期区间的日志索引，一般按照日期来创建索引，例如：yth-2018.07.02
     * @param index
     * @param beginTime
     * @param endTime
     * @return
     */
    private String[] indexs(String index,String beginTime,String endTime){
        List<String> indexs = new ArrayList<>();
        Date begin = DateUtils.getInstance().toDateStr(beginTime);
        Date end =  DateUtils.getInstance().toDateStr(endTime);
        int day = DateUtils.getInstance().differentDaysByMillisecond(begin,end);//计算两个时间相差的天数，如果返回是0，则是查询当天的索引
        if(day == 0){
            indexs.add(index+"-"+DateUtils.dateToStr(begin));
        }else{
            for (int i=0 ; i <= day;i++){
                indexs.add(index+"-"+DateUtils.plusDay(i,begin));
            }
        }

        //遍历list，判断indexName 是否存在
        Iterator<String> iterator = indexs.iterator();
        while (iterator.hasNext()){
            if(!isExist(iterator.next())){
                iterator.remove();
            }
        }
        String[] results = indexs.toArray(new String[indexs.size()]);
        logger.info("索引集合："+indexs.toString());
        return results;
    }


    /**
     * 根据IndicesExistsResponse对象的isExists()方法的boolean返回值可以判断索引库是否存在.
     * @param indexName
     * @return
     */
    private Boolean isExist(String indexName){
        //如果传入的indexName不存在会出现异常.可以先判断索引是否存在：
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

        IndicesExistsResponse inExistsResponse = client.buildClient().admin().indices()
                .exists(inExistsRequest).actionGet();

        Boolean result = inExistsResponse.isExists();
        return result;
    }



    @Override
    public Map<String, Object> queryLogs(Map<String, Object> params) {
        logger.info("查询日志开始==============");
        Map<String,Object> result = new HashMap<>();
        long sTime = System.currentTimeMillis();
        String index = (String) params.get("index");  //日志索引
        String content = (String) params.get("content"); //查询内容
        String beginTime = (String) params.get("beginTime");//开始时间 yyyy-mm-dd hh:mm:ss
        String endTime = (String) params.get("endTime");//结束时间
        int page = (int) params.get("page");//当前页码
        int pageSize = (int) params.get("pageSize");//页数

        //处理索引
        String[] indexs = this.indexs(index,beginTime,endTime);

        logger.info("查询参数:"+params.toString());
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        if(content!=null&&!content.equals("")){
            //时间范围
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp")
                    .from(DateUtils.getInstance().toSolrTime(beginTime))
                    .to(DateUtils.getInstance().toSolrTime(endTime))
                    .includeLower(true)
                    .includeUpper(false);
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(content);
            boolQueryBuilder.must(rangeQueryBuilder).must(queryStringQueryBuilder);
        }else{
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }
        SearchResponse response=client.buildClient().prepareSearch(indexs).setTypes("doc")
                .setQuery(boolQueryBuilder)
                .setFrom((page-1)*pageSize).setSize(pageSize)
                .setExplain(true)
                .get();

        SearchHits hits = response.getHits();
        SearchHit[] searchHitArr =  hits.getHits();

        Long totalHits = hits.getTotalHits();//总条数
        List<JSONObject> sources = new ArrayList<>();
        for (SearchHit searchHit:searchHitArr) {
            JSONObject jsonObject = JSON.parseObject(searchHit.getSourceAsString());
            sources.add(jsonObject);
        }

        long eTime = System.currentTimeMillis();
        logger.info("查询返回结果："+response);
        result.put("total",totalHits);
        result.put("row",sources);
        logger.info("程序运行时间：" + (eTime - sTime) + "ms");
       return result;
    }
}

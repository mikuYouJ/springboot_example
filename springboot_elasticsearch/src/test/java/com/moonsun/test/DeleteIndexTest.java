package com.moonsun.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;

/**
 * Created by 82610 on 2018/6/14.
 */
public class DeleteIndexTest {
    public static void main(String[] args) throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("10.21.89.52", 9200, "http")
        ));

        DeleteIndexRequest request = new DeleteIndexRequest("twitter");

        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");

        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.masterNodeTimeout("1m");

        //同步删除
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(request);

    }
}

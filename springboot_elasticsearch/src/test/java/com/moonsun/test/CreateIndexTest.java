package com.moonsun.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * Created by 82610 on 2018/6/13.
 */
public class CreateIndexTest {

    public static void main(String[] args) throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("10.21.89.52", 9200, "http"),
                new HttpHost("10.21.89.52", 9201, "http")
        ));

        //创建一个索引请求参数
        CreateIndexRequest request = new CreateIndexRequest("twitter");

        //索引设置参数
        request.settings(
                Settings.builder().
                        put("index.number_of_shards", "3")
                        .put("index.number_of_replicas", "2")
        );

        //可以用其文档类型的映射来创建索引
        request.mapping("tweet",
                "  {\n" +
                        "    \"tweet\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"message\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }",
                XContentType.JSON);

        request.alias(
                new Alias("twitter_alias")
        );

        request.timeout(TimeValue.timeValueMinutes(2));
        request.timeout("2m");

        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.masterNodeTimeout("1m");

        //同步执行
        CreateIndexResponse createIndexResponse = client.indices().create(request);

//        client.indices().createAsync(request, listener);
        //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
        boolean acknowledged = createIndexResponse.isAcknowledged();//指示是否所有节点都已确认请求
        System.out.println(acknowledged);
        client.close();
    }
}

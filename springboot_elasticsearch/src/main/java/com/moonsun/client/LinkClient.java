package com.moonsun.client;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by youj on 2018/6/20.
 */
@Component
public class LinkClient {

    private static final Logger LOG = LoggerFactory.getLogger(LinkClient.class);

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;

    public RestHighLevelClient buildClient(){
        if (restHighLevelClient == null){
            try {
                restHighLevelClient = new RestHighLevelClient(
                        RestClient.builder(
                                new HttpHost(
                                        clusterNodes.split(":")[0],
                                        Integer.parseInt(clusterNodes.split(":")[1]),
                                        "http")));
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return restHighLevelClient;
    }

    public void destroy() throws Exception {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            LOG.error("Error closing ElasticSearch client: ", e);
        }
    }
}

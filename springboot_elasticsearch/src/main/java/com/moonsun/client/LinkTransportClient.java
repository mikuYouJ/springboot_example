package com.moonsun.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by 82610 on 2018/6/21.
 */
@Component
public class LinkTransportClient {
    private static final Logger LOG = LoggerFactory.getLogger(LinkTransportClient.class);

    @Value("${spring.data.elasticsearch.cluster-node-ip}")
    private String clusterNodeIp;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.port}")
    private Integer port;

    private TransportClient transportClient;

    public TransportClient buildClient(){
        if (transportClient == null){
            try {
                Settings settings = Settings.builder()
                        .put("cluster.name", clusterName).build();

                transportClient = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName(clusterNodeIp), port));
            }catch (Exception e){
                LOG.error("创建transportClient异常"+e.getMessage());
            }

        }

        return transportClient;
    }

    public void destory(){
        try {
            if (transportClient == null){
                transportClient.close();
            }
        }catch (Exception e){
            LOG.error("transportClient关闭客户端异常:"+e.getMessage());
        }

    }
}

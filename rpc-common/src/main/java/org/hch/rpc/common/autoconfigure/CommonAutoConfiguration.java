package org.hch.rpc.common.autoconfigure;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.hch.rpc.common.config.ApplicationProperties;
import org.hch.rpc.common.config.ZookeeperProperties;
import org.hch.rpc.common.protocol.marshalling.Marshalling;
import org.hch.rpc.common.protocol.marshalling.impl.JsonMarshalling;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenghao on 9/7/16.
 */
@Configuration
@EnableConfigurationProperties({ApplicationProperties.class,ZookeeperProperties.class})
public class CommonAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public CuratorFramework curatorFramework(ZookeeperProperties properties, RetryPolicy retryPolicy) throws InterruptedException {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework curator = builder
                .retryPolicy(retryPolicy)
                .connectString(properties.getConnectString())
                .build();
        curator.start();
        curator.blockUntilConnected(properties.getBlockUntilConnectedWait(), properties.getBlockUntilConnectedUnit());
        return curator;
    }
    @Bean
    @ConditionalOnMissingBean
    public RetryPolicy exponentialBackoffRetry(ZookeeperProperties zookeeperProperties) {
        return new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                zookeeperProperties.getMaxRetries(),
                zookeeperProperties.getMaxSleepMs());
    }


    @Bean
    @ConditionalOnMissingBean
    public Marshalling marshalling(){
        return new JsonMarshalling();
    }
}

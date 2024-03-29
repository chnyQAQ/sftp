package com.xzixi.sftp.autoconfigure;

import com.xzixi.sftp.pool.SftpAbandonedConfig;
import com.xzixi.sftp.pool.SftpFactory;
import com.xzixi.sftp.pool.SftpPool;
import com.xzixi.sftp.pool.SftpPoolConfig;
import org.apache.commons.pool2.impl.DefaultEvictionPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;

@Configuration
@ConditionalOnClass(SftpPool.class)
@EnableConfigurationProperties(SftpPoolProperties.class)
public class SftpPoolAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SftpFactory sftpFactory(SftpPoolProperties sftpPoolProperties) {
        return new SftpFactory.Builder()
                .host(sftpPoolProperties.getHost())
                .port(sftpPoolProperties.getPort())
                .username(sftpPoolProperties.getUsername())
                .password(sftpPoolProperties.getPassword())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SftpPoolConfig sftpPoolConfig(SftpPoolProperties sftpPoolProperties) {
        SftpPoolProperties.Pool pool = sftpPoolProperties.getPool();
        return new SftpPoolConfig.Builder()
                .maxTotal(pool.getMaxTotal())
                .maxIdle(pool.getMaxIdle())
                .minIdle(pool.getMinIdle())
                .lifo(pool.isLifo())
                .fairness(pool.isFairness())
                .maxWaitMillis(pool.getMaxWaitMillis())
                .minEvictableIdleTimeMillis(pool.getMinEvictableIdleTimeMillis())
                .evictorShutdownTimeoutMillis(pool.getEvictorShutdownTimeoutMillis())
                .softMinEvictableIdleTimeMillis(pool.getSoftMinEvictableIdleTimeMillis())
                .numTestsPerEvictionRun(pool.getNumTestsPerEvictionRun())
                .evictionPolicy(null)
                .evictionPolicyClassName(DefaultEvictionPolicy.class.getName())
                .testOnCreate(pool.isTestOnCreate())
                .testOnBorrow(pool.isTestOnBorrow())
                .testOnReturn(pool.isTestOnReturn())
                .testWhileIdle(pool.isTestWhileIdle())
                .timeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRunsMillis())
                .blockWhenExhausted(pool.isBlockWhenExhausted())
                .jmxEnabled(pool.isJmxEnabled())
                .jmxNamePrefix(pool.getJmxNamePrefix())
                .jmxNameBase(pool.getJmxNameBase())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SftpAbandonedConfig sftpAbandonedConfig(SftpPoolProperties sftpPoolProperties) {
        SftpPoolProperties.Abandoned abandoned = sftpPoolProperties.getAbandoned();
        return new SftpAbandonedConfig.Builder()
                .removeAbandonedOnBorrow(abandoned.isRemoveAbandonedOnBorrow())
                .removeAbandonedOnMaintenance(abandoned.isRemoveAbandonedOnMaintenance())
                .removeAbandonedTimeout(abandoned.getRemoveAbandonedTimeout())
                .logAbandoned(abandoned.isLogAbandoned())
                .requireFullStackTrace(abandoned.isRequireFullStackTrace())
                .logWriter(new PrintWriter(System.out))
                .useUsageTracking(abandoned.isUseUsageTracking())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SftpPool sftpPool(SftpFactory sftpFactory,
                             SftpPoolConfig sftpPoolConfig,
                             SftpAbandonedConfig sftpAbandonedConfig) {
        return new SftpPool(sftpFactory, sftpPoolConfig, sftpAbandonedConfig);
    }

}

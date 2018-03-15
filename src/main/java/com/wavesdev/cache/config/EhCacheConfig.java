package com.wavesdev.cache.config;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pranavan on 3/13/18.
 */
@Configuration
public class EhCacheConfig {

    @Value("${cache.ehcache.config.timeToLiveSeconds}")
    private Long timeToLiveSeconds;
    @Value("${cache.ehcache.config.maxIdleSeconds}")
    private Long maxIdleSeconds;
    @Value("${cache.ehcache.config.maxSize}")
    private Long maxSize;
    @Value("${cache.ehcache.config.evictionPolicy}")
    private String evictionPolicy;
    @Bean
    public CacheManager cacheManager() {

       //return new EhCacheCacheManager(ehCacheCacheManager().getObject());
        return new EhCacheCacheManager(ehCacheManager());
    }
//xml config
    /*@Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }
    */

    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("product");
        cacheConfiguration.setMemoryStoreEvictionPolicy(evictionPolicy);
        cacheConfiguration.setMaxEntriesLocalHeap(maxSize);
        cacheConfiguration.setTimeToLiveSeconds(timeToLiveSeconds);
        cacheConfiguration.setTimeToIdleSeconds(0);
        cacheConfiguration.setTransactionalMode("off");
        cacheConfiguration.setEternal(false);
        PersistenceConfiguration persistenceConfiguration=new PersistenceConfiguration();
        persistenceConfiguration.setStrategy("none");
        cacheConfiguration.addPersistence(persistenceConfiguration);
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);


        return net.sf.ehcache.CacheManager.newInstance(config);
    }
}

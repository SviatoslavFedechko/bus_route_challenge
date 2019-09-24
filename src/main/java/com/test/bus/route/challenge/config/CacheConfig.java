package com.test.bus.route.challenge.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return cacheManager;
    }
    @Bean
    public RouteDataBootstrapCacheLoaderFactory getRouteDataBootstrapCacheLoaderFactory() {
        return new RouteDataBootstrapCacheLoaderFactory();
    }

    @Bean
    public EhCacheFactoryBean ehCacheFactory() {
        EhCacheFactoryBean ehCacheFactory = new EhCacheFactoryBean();
        ehCacheFactory.setCacheManager(ehCacheManagerFactoryBean().getObject());
        ehCacheFactory.setBootstrapCacheLoader(getRouteDataBootstrapCacheLoaderFactory());
        return ehCacheFactory;
    }

}

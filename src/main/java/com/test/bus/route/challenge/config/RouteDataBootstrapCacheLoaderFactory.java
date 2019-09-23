package com.test.bus.route.challenge.config;

import com.test.bus.route.challenge.service.DataService;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import net.sf.ehcache.bootstrap.BootstrapCacheLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class RouteDataBootstrapCacheLoaderFactory extends BootstrapCacheLoaderFactory implements BootstrapCacheLoader {

    private Logger logger = LoggerFactory.getLogger(RouteDataBootstrapCacheLoaderFactory.class);

    @Autowired
    private DataService dataService;

    public RouteDataBootstrapCacheLoaderFactory() {
        super();
    }

    @Override
    public BootstrapCacheLoader createBootstrapCacheLoader(Properties properties) {
        return null;
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void load(Ehcache myCache) {
        try {
            logger.info("route data caching started in MyBootstrapCacheLoaderFactory");
            long start = System.currentTimeMillis();

//            dataService.getBusRoutesData();

            logger.info("route data caching finished in MyBootstrapCacheLoaderFactory");
            long end = System.currentTimeMillis();
            String searchTime = String.format("Structuring and caching data for search finished in %s seconds: ",
                    String.valueOf((end - start) / 1000.0));
            logger.info(searchTime);
        } catch (Exception e) {
            logger.error("cache load error", e);
        }
    }

}

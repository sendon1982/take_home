package com.takehome.config;

import com.takehome.dao.TripSearchDao;
import com.takehome.dao.TripSearchDaoImpl;
import com.takehome.service.TripSearchService;
import com.takehome.service.TripSearchServiceImpl;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TripSearchService tripSearchService(TripSearchDao tripSearchDao) {
        return new TripSearchServiceImpl(tripSearchDao);
    }

    @Bean
    public TripSearchDao tripSearchDao(DefaultDSLContext context) {
        return new TripSearchDaoImpl(context);
    }
}

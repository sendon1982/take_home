package com.takehome.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.takehome.dao.TripSearchDao;
import com.takehome.gen.model.MedallionTrip;
import com.takehome.gen.model.TripSearchResponse;
import com.takehome.gen.model.TripSearchResponseData;
import com.takehome.model.TripInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;

@Slf4j
@CacheConfig(cacheNames = com.takehome.config.CacheConfig.MEDALLIONS_CACHE)
public class TripSearchServiceImpl implements TripSearchService {

    private final TripSearchDao tripSearchDao;

    public TripSearchServiceImpl(TripSearchDao tripSearchDao) {
        this.tripSearchDao = tripSearchDao;
    }

    @Override
    @Cacheable
    public TripSearchResponse findTripInfosByIdAndDate(Set<String> medallions, LocalDate pickupDate) {

        if (CollectionUtils.isEmpty(medallions)) {
            return buildEmptyTripSearchResponse();
        }

        log.info("Fetch from Database based on medallions and pickupDate [{}]", pickupDate);
        List<TripInfo> tripInfoList = tripSearchDao.findTripInfosByIdAndDate(medallions, pickupDate);

        if (CollectionUtils.isEmpty(tripInfoList)) {
            return buildEmptyTripSearchResponse();
        }

        Map<String, Long> medallionTripCountMap = tripInfoList.stream().collect(Collectors.groupingBy(TripInfo::getMedallion, Collectors.counting()));

        List<MedallionTrip> medallionTrips = new ArrayList<>();

        for (String medallion : medallions) {
            MedallionTrip medallionTrip = new MedallionTrip();
            medallionTrip.setMedallion(medallion);
            medallionTrip.setTripCount(medallionTripCountMap.getOrDefault(medallion, 0L));

            medallionTrips.add(medallionTrip);
        }

        TripSearchResponseData responseData = new TripSearchResponseData();
        responseData.setMedallionTrips(medallionTrips);
        responseData.setPickupDate(pickupDate);

        TripSearchResponse response = new TripSearchResponse();
        response.setData(responseData);

        return response;
    }

    private TripSearchResponse buildEmptyTripSearchResponse() {
        TripSearchResponseData responseData = new TripSearchResponseData();

        TripSearchResponse response = new TripSearchResponse();
        response.setData(responseData);

        return response;
    }
}
